package com.speakupcambridge.service;

import com.speakupcambridge.entity.MailchimpUpdateLog;
import com.speakupcambridge.enums.DataActionType;
import com.speakupcambridge.event.CrudAction;
import com.speakupcambridge.model.mailchimp.MailchimpPerson;
import com.speakupcambridge.model.mailchimp.MailchimpLocalPerson;
import com.speakupcambridge.model.mailchimp.MailchimpRemotePerson;
import com.speakupcambridge.model.Person;
import com.speakupcambridge.model.enums.SubscriptionStatus;
import com.speakupcambridge.model.mapper.OneToOneMapper;
import com.speakupcambridge.model.mapper.PersonMapper;
import com.speakupcambridge.repository.MailchimpUpdateLogJpaRepository;
import com.speakupcambridge.repository.mailchimp.LocalMailchimpPersonJpaRepository;
import com.speakupcambridge.repository.mailchimp.RemoteMailchimpListsRepository;
import com.speakupcambridge.repository.mailchimp.RemoteMailchimpPersonJpaRepository;
import com.speakupcambridge.repository.mailchimp.RemoteMailchimpPersonRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Logger;

@Service
public class MailchimpService {
  private static final Logger LOGGER = Logger.getLogger(MailchimpService.class.getName());
  private final RemoteMailchimpPersonRepository remoteMailchimpPersonRepository;
  private final RemoteMailchimpListsRepository remoteMailchimpListsRepository;
  private final LocalMailchimpPersonJpaRepository localMailchimpPersonJpaRepository;
  private final RemoteMailchimpPersonJpaRepository remoteMailchimpPersonJpaRepository;
  private final MailchimpUpdateLogJpaRepository mailchimpUpdateLogJpaRepository;
  private final ApplicationEventPublisher eventPublisher;

  public MailchimpService(
      RemoteMailchimpPersonRepository remoteMailchimpPersonRepository,
      RemoteMailchimpListsRepository remoteMailchimpListsRepository,
      LocalMailchimpPersonJpaRepository localMailchimpPersonJpaRepository,
      RemoteMailchimpPersonJpaRepository remoteMailchimpPersonJpaRepository,
      MailchimpUpdateLogJpaRepository mailchimpUpdateLogJpaRepository,
      ApplicationEventPublisher eventPublisher) {
    this.remoteMailchimpPersonRepository = remoteMailchimpPersonRepository;
    this.remoteMailchimpListsRepository = remoteMailchimpListsRepository;
    this.localMailchimpPersonJpaRepository = localMailchimpPersonJpaRepository;
    this.remoteMailchimpPersonJpaRepository = remoteMailchimpPersonJpaRepository;
    this.mailchimpUpdateLogJpaRepository = mailchimpUpdateLogJpaRepository;
    this.eventPublisher = eventPublisher;
  }

  public List<MailchimpPerson> fetchPersons(String listId) {
    return this.remoteMailchimpPersonRepository.findAll(listId);
  }

  public List<MailchimpPerson> fetchPersons() {
    return this.remoteMailchimpPersonRepository.findAll();
  }

  public List<String> fetchListIds() {
    return this.remoteMailchimpListsRepository.findAllListIds();
  }

  public void syncDatabase() {
    List<MailchimpPerson> personList = this.fetchPersons();
    List<MailchimpRemotePerson> remotePersonList =
        personList.stream().map(OneToOneMapper.INSTANCE::toMailchimpRemotePerson).toList();
    this.remoteMailchimpPersonJpaRepository.saveAll(remotePersonList);
  }

  public void generateUpdatedLocalTableFromPersonsList(List<Person> personsList) {
    // Delete the logs
    this.mailchimpUpdateLogJpaRepository.deleteAll();
    // Copy remote mailchimp table to outgoing table for compare/overwrite/update
    this.copyRemoteToLocal();

    // Loop through persons list from airtable and attempt to find each in the
    // existing (source) Mailchimp table:
    // - If it exists, update it.
    // - Otherwise, if subscribed and with a valid email, add it.
    for (Person person : personsList) {
      Optional<String> oEmail = Optional.ofNullable(person.getEmail());
      if (oEmail.isEmpty()) {
        MailchimpService.LOGGER.info(
            String.format(
                "In Mailchimp update: No email address found for '%s'", person.getName()));
        if (person.getReminderEmails()) {
          MailchimpService.LOGGER.warning(
              String.format(
                  "In Mailchimp update: Cannot subscribe '%s' without a registered email address",
                  person.getName()));
        }
        continue;
      }

      // Attempt to find by email
      this.localMailchimpPersonJpaRepository
          .findByEmail(oEmail.get())
          .ifPresentOrElse(
              match -> {
                // Attempt to update the Mailchimp record
                updateWith(match, person);

                // Save it
                this.saveAndLog(match, DataActionType.UPDATE);
              },
              () -> {
                // Match not found --
                // Add the person if they're subscribed
                if (person.getReminderEmails()) {
                  MailchimpLocalPerson newPerson =
                      new MailchimpLocalPerson(PersonMapper.toMailchimpPerson(person));

                  this.saveAndLog(newPerson, DataActionType.CREATE);
                }
              });
    }
  }

  public List<MailchimpUpdateLog> getAllChanges() {
    return this.mailchimpUpdateLogJpaRepository.findAll();
  }

  private void copyRemoteToLocal() {
    // First clear the table
    this.localMailchimpPersonJpaRepository.deleteAll();

    // Copy the remote person list to the local table
    List<MailchimpRemotePerson> remotePersonList =
        this.remoteMailchimpPersonJpaRepository.findAll();
    List<MailchimpLocalPerson> localPersonList =
        remotePersonList.stream().map(OneToOneMapper.INSTANCE::toMailchimpLocalPerson).toList();

    this.localMailchimpPersonJpaRepository.saveAll(localPersonList);
  }

  private void updateWith(MailchimpLocalPerson mailchimpLocalPerson, Person person) {
    // Check subscription status against Mailchimp's --
    SubscriptionStatus matchStatus =
        SubscriptionStatus.fromString(
            Objects.requireNonNull(
                mailchimpLocalPerson.getStatus(),
                String.format(
                    "Mailchimp member '%s' has no subscription status",
                    mailchimpLocalPerson.getFullName())));

    switch (matchStatus) {
      case SUBSCRIBED -> {
        // If we have unsubscribed them, we may proceed with update.
        // This is ok regardless of Mailchimp subscription status
        if (!matchStatus.equals(SubscriptionStatus.fromBool(person.getReminderEmails()))) {
          MailchimpService.LOGGER.info(
              String.format("Unsubscribing member '%s' from receiving emails.", person.getName()));
        }
        mailchimpLocalPerson.overwriteWith(PersonMapper.toMailchimpPerson(person), false);
      }
      case UNSUBSCRIBED -> {
        // Inform that the person has unsubscribed from the mailing service
        MailchimpService.LOGGER.info(
            String.format("'%s' has unsubscribed from emails.", person.getName()));
        // Remote is unsubscribed, but we have them flagged for emails. Forbidden! Issue severe
        // warning that master DB must be updated to reflect.
        // Do not update.
        if (person.getReminderEmails()) {
          MailchimpService.LOGGER.severe(
              String.format(
                  "'%s' is marked to receive emails but has unsubscribed."
                      + " Database should be updated to reflect that they no longer wish to receive emails.",
                  person.getName()));
        }
      }
      case CLEANED ->
          // Remote email is not valid. Issue warning that the email on file is incorrect or has
          // changed.
          // Do not update.
          MailchimpService.LOGGER.warning(
              String.format(
                  "Registered email '%s' for '%s' is incorrect or no longer in service.",
                  person.getEmail(), person.getName()));
    }
  }

  private void saveAndLog(MailchimpLocalPerson person, DataActionType actionType) {
    this.localMailchimpPersonJpaRepository.save(person);

    // Generate a log description
    String description;
    if (actionType.equals(DataActionType.CREATE)) {
      description = generateCreateDescription(person);
    } else {
      Optional<MailchimpRemotePerson> oOriginal =
          remoteMailchimpPersonJpaRepository.findByEmail(person.getEmail());
      if (oOriginal.isEmpty()) {
        throw new RuntimeException(
            String.format(
                "Somehow email '%s' can't be found in originating Mailchimp repository",
                person.getEmail()));
      }
      description = generateUpdateDescription(oOriginal.get(), person);
    }

    this.publishCrudEvent(actionType, person.getId(), person.getFullName(), description);
    this.saveCrudLog(actionType, person.getId(), person.getFullName(), description);
  }

  private void publishCrudEvent(
      DataActionType actionType, String id, String name, String description) {
    this.eventPublisher.publishEvent(
        new CrudAction(
            this,
            actionType,
            id,
            name,
            MailchimpLocalPerson.class,
            LocalMailchimpPersonJpaRepository.class,
            description));
  }

  private void saveCrudLog(DataActionType actionType, String id, String name, String description) {
    this.mailchimpUpdateLogJpaRepository.save(
        new MailchimpUpdateLog(
            actionType,
            id,
            name,
            MailchimpLocalPerson.class.toString(),
            RemoteMailchimpPersonJpaRepository.class.toString(),
            description));
  }

  private String generateCreateDescription(MailchimpPerson person) {
    List<String> fieldDescriptions = new ArrayList<>();
    // Loop through each field
    for (Field field : person.getClass().getDeclaredFields()) {
      try {
        // Make the field accessible
        field.setAccessible(true);

        // Get the field value
        Object value = field.get(person);

        // Print the field name and value if the value is not null
        if (value != null) {
          fieldDescriptions.add(String.format("%s: %s", field.getName(), value));
        }
      } catch (IllegalAccessException e) {
        throw new RuntimeException(
            "Attempt to generate CREATE entity description using reflection failed", e);
        //        e.printStackTrace();
      }
    }
    return String.join(", ", fieldDescriptions);
  }

  private String generateUpdateDescription(MailchimpPerson before, MailchimpPerson after) {
    List<String> fieldDescriptions = new ArrayList<>();
    // Loop through each field
    for (Field field : before.getClass().getDeclaredFields()) {
      try {
        // Make the field accessible
        field.setAccessible(true);

        // Get the field values
        Object beforeValue = field.get(before);
        Object afterValue = field.get(after);

        // Print the change if the values are different (and not null)
        if (Objects.nonNull(beforeValue) && beforeValue.equals(afterValue)) {
          fieldDescriptions.add(
              String.format("%s: %s->%s", field.getName(), beforeValue, afterValue));
        }
      } catch (IllegalAccessException e) {
        throw new RuntimeException(
            "Attempt to generate UPDATE entity description using reflection failed", e);
        //        e.printStackTrace();
      }
    }
    return String.join(", ", fieldDescriptions);
  }
}

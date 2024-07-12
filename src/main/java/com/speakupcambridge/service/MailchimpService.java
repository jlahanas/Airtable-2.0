package com.speakupcambridge.service;

import com.speakupcambridge.model.MailchimpPerson;
import com.speakupcambridge.model.MailchimpLocalPerson;
import com.speakupcambridge.model.MailchimpRemotePerson;
import com.speakupcambridge.model.Person;
import com.speakupcambridge.model.enums.SubscriptionStatus;
import com.speakupcambridge.model.mapper.OneToOneMapper;
import com.speakupcambridge.model.mapper.PersonMapper;
import com.speakupcambridge.repository.*;
import org.springframework.stereotype.Service;

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
  private final LocalPersonRepository localPersonRepository;

  public MailchimpService(
      RemoteMailchimpPersonRepository remoteMailchimpPersonRepository,
      RemoteMailchimpListsRepository remoteMailchimpListsRepository,
      LocalMailchimpPersonJpaRepository localMailchimpPersonJpaRepository,
      RemoteMailchimpPersonJpaRepository remoteMailchimpPersonJpaRepository,
      LocalPersonRepository localPersonRepository) {
    this.remoteMailchimpPersonRepository = remoteMailchimpPersonRepository;
    this.remoteMailchimpListsRepository = remoteMailchimpListsRepository;
    this.localMailchimpPersonJpaRepository = localMailchimpPersonJpaRepository;
    this.remoteMailchimpPersonJpaRepository = remoteMailchimpPersonJpaRepository;
    this.localPersonRepository = localPersonRepository;
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

  public void generateTableFromRawData(boolean syncData) {
    if (syncData) {
      syncDatabase();
    }

    // Copy remote mailchimp table to outgoing table for compare/overwrite/update
    this.copyRemoteToLocal();

    // Loop through persons list from airtable and attempt to find each in the
    // existing (source) Mailchimp table:
    // - If it exists, update it.
    // - Otherwise, if subscribed and with a valid email, add it.
    List<Person> personList = this.localPersonRepository.findAll();

    for (Person person : this.localPersonRepository.findAll()) {
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
      String email = oEmail.get();

      // Attempt to find by email
      Optional<MailchimpLocalPerson> match = this.localMailchimpPersonJpaRepository.findById(email);
      if (match.isPresent()) {
        // Update --
        boolean update = false;

        // Check subscription status against Mailchimp's --
        SubscriptionStatus matchStatus =
            SubscriptionStatus.fromString(
                Objects.requireNonNull(
                    match.get().getStatus(),
                    String.format(
                        "Mailchimp member '%s' has no subscription status",
                        match.get().getFullName())));

        switch (matchStatus) {
          case SUBSCRIBED -> {
            // If we have unsubscribed them, simply update Mailchimp to reflect this
            // The record will be updated; notify and continue
            if (!matchStatus.equals(SubscriptionStatus.fromBool(person.getReminderEmails()))) {
              MailchimpService.LOGGER.info(
                  String.format(
                      "Unsubscribing member '%s' from receiving emails.", person.getName()));
            }
            update = true;
          }
          case UNSUBSCRIBED -> {
            // Inform that the person has unsubscribed from the mailing service
            MailchimpService.LOGGER.info(
                String.format("'%s' has unsubscribed from emails.", person.getName()));
            // Remote is unsubscribed, but we have them flagged for emails. Forbidden! Issue severe
            // warning that master DB must be updated to reflect. Do not update.
            if (person.getReminderEmails()) {
              MailchimpService.LOGGER.severe(
                  String.format(
                      "'%s' is marked to receive emails but has unsubscribed."
                          + " Database should be updated to reflect that they no longer wish to receive emails.",
                      person.getName()));
            }
          }
          case CLEANED -> {
            // Remote email is not valid. Issue warning that the email on file is incorrect or has
            // changed.
            MailchimpService.LOGGER.warning(
                String.format(
                    "Registered email '%s' for '%s' is incorrect or no longer in service.",
                    person.getEmail(), person.getName()));
          }
        }

        if (update) {
          match.get().overwriteWith(PersonMapper.toMailchimpPerson(person), false);
          //
          // this.localMailchimpPersonJpaRepository.save(PersonMapper.toMailchimpLocalPerson(person));
          //
          // this.localMailchimpPersonJpaRepository.save(PersonMapper.overwriteMailchimpPerson(person))
        }
      } else {
        // Match not found --
        // Add the person if they're subscribed
        if (person.getReminderEmails()) {
          MailchimpLocalPerson newPerson =
              new MailchimpLocalPerson(PersonMapper.toMailchimpPerson(person));
          this.localMailchimpPersonJpaRepository.save(newPerson);
        }
      }
    }
  }

  private void copyRemoteToLocal() {
    List<MailchimpRemotePerson> remotePersonList =
        this.remoteMailchimpPersonJpaRepository.findAll();
    List<MailchimpLocalPerson> localPersonList =
        remotePersonList.stream().map(OneToOneMapper.INSTANCE::toMailchimpLocalPerson).toList();

    this.localMailchimpPersonJpaRepository.saveAll(localPersonList);
  }
}

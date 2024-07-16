package com.speakupcambridge.model.mapper;

import com.speakupcambridge.model.airtable.AirtablePerson;
import com.speakupcambridge.model.mailchimp.MailchimpPerson;
import com.speakupcambridge.model.Person;
import com.speakupcambridge.model.enums.ActiveStatus;
import com.speakupcambridge.model.enums.NullableBoolString;
import com.speakupcambridge.model.enums.PersonType;
import com.speakupcambridge.model.enums.SubscriptionStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class PersonMapper {
  public static Person toPerson(AirtablePerson airtablePerson) {
    return new Person(
        null,
        airtablePerson.getId(),
        airtablePerson.getFields().getName(),
        airtablePerson.getFields().getFirstName(),
        airtablePerson.getFields().getLastName(),
        PersonType.fromString(airtablePerson.getFields().getType()),
        ActiveStatus.fromString(airtablePerson.getFields().getStatus()),
        ActiveStatus.fromString(airtablePerson.getFields().getAttendanceStatus()),
        airtablePerson.getFields().getEmail(),
        airtablePerson.getFields().getAddress(),
        NullableBoolString.fromString(airtablePerson.getFields().getReminderEmails()).toBoolean(),
        parseLocalDateNullable(airtablePerson.getFields().getLastAttendedDate()),
        null,
        null,
        parseLocalDateNullable(airtablePerson.getFields().getLastSpeechDate()),
        null,
        parseLocalDateNullable(airtablePerson.getFields().getDuesLastPaid()),
        airtablePerson.getFields().getPhone(),
        parseLocalDateTimeNullable(airtablePerson.getCreatedTime()),
        null,
        null,
        null);
  }

  public static AirtablePerson toAirtablePerson(Person person) {
    return new AirtablePerson(
        person.getAirtableId(),
        person.getCreatedTime().toString(),
        new AirtablePerson.AirtablePersonFields(
            person.getFirstName(),
            person.getLastName(),
            person.getName(),
            person.getEmail(),
            person.getAddress(),
            person.getPhone(),
            person.getType().toString(),
            person.getMembershipStatus().toString(),
            person.getAttendanceStatus().toString(),
            person.getDuesLastPaid().toString(),
            null,
            null,
            NullableBoolString.fromBoolean(person.getReminderEmails()).toString(),
            person.getLastAttendedDate().toString(),
            person.getLastSpeechDate().toString(),
            null,
            null,
            null,
            null,
            null));
  }

  public static Person toPerson(MailchimpPerson mailchimpPerson) {
    return new Person(
        null,
        mailchimpPerson.getId(),
        mailchimpPerson.getFullName(),
        mailchimpPerson.getMergeFields().getFName(),
        mailchimpPerson.getMergeFields().getLName(),
        PersonType.fromString(mailchimpPerson.getMergeFields().getMMerge5()),
        ActiveStatus.fromString(mailchimpPerson.getMergeFields().getMMerge6()),
        null,
        mailchimpPerson.getEmail(),
        null,
        SubscriptionStatus.fromString(mailchimpPerson.getStatus())
            .equals(SubscriptionStatus.SUBSCRIBED),
        null,
        null,
        null,
        null,
        null,
        parseLocalDateNullable(mailchimpPerson.getMergeFields().getMMerge7()),
        mailchimpPerson.getSmsPhoneNumber(),
        null,
        null,
        null,
        null);
  }

  public static MailchimpPerson toMailchimpPerson(Person person) {
    return new MailchimpPerson(
        null,
        person.getEmail(),
        null,
        null,
        person.getName(),
        null,
        null,
        SubscriptionStatus.fromBool(person.getReminderEmails()).toString(),
        null,
        null,
        null,
        null,
        null,
        new MailchimpPerson.MailchimpPersonMergeFields(
            person.getFirstName(),
            person.getLastName(),
            person.getAddress(),
            person.getPhone(),
            Optional.ofNullable(person.getType()).map(PersonType::toString).orElse(null),
            Optional.ofNullable(person.getMembershipStatus())
                .map(ActiveStatus::toString)
                .orElse(null),
            Optional.ofNullable(person.getDuesLastPaid()).map(LocalDate::toString).orElse(null),
            null,
            null));
  }

  //  private static MailchimpLocalPerson overwriteMailchimpPerson(
  //      MailchimpLocalPerson target, MailchimpLocalPerson source) {
  //    return new MailchimpLocalPerson(
  //        Optional.ofNullable(source.getId()).orElse(target.getId()),
  //        Optional.ofNullable(source.getEmail()).orElse(target.getEmail()),
  //        Optional.ofNullable(source.getUniqueEmailId()).orElse(target.getUniqueEmailId()),
  //        Optional.ofNullable(source.getContactId()).orElse(target.getContactId()),
  //        Optional.ofNullable(source.getFullName()).orElse(target.getFullName()),
  //        Optional.ofNullable(source.getWebId()).orElse(target.getWebId()),
  //        Optional.ofNullable(source.getEmailType()).orElse(target.getEmailType()),
  //        Optional.ofNullable(source.getStatus()).orElse(target.getStatus()),
  //
  // Optional.ofNullable(source.getUnsubscribeReason()).orElse(target.getUnsubscribeReason()),
  //        Optional.ofNullable(source.getConsentsToOneToOneMessaging())
  //            .orElse(target.getConsentsToOneToOneMessaging()),
  //        Optional.ofNullable(source.getSmsPhoneNumber()).orElse(target.getSmsPhoneNumber()),
  //        Optional.ofNullable(source.getSmsSubscriptionLastUpdated())
  //            .orElse(target.getSmsSubscriptionStatus()),
  //        Optional.ofNullable(source.getSmsSubscriptionLastUpdated())
  //            .orElse(target.getSmsSubscriptionLastUpdated()),
  //        new MailchimpPerson.MailchimpPersonMergeFields(
  //            Optional.ofNullable(source.getMergeFields().getFName())
  //                .orElse(target.getMergeFields().getFName()),
  //            Optional.ofNullable(source.getMergeFields().getLName())
  //                .orElse(target.getMergeFields().getLName()),
  //            Optional.ofNullable(source.getMergeFields().getAddress())
  //                .orElse(target.getMergeFields().getAddress()),
  //            Optional.ofNullable(source.getMergeFields().getPhone())
  //                .orElse(target.getMergeFields().getPhone()),
  //            Optional.ofNullable(source.getMergeFields().getMMerge5())
  //                .orElse(target.getMergeFields().getMMerge5()),
  //            Optional.ofNullable(source.getMergeFields().getMMerge6())
  //                .orElse(target.getMergeFields().getMMerge6()),
  //            Optional.ofNullable(source.getMergeFields().getMMerge7())
  //                .orElse(target.getMergeFields().getMMerge7()),
  //            Optional.ofNullable(source.getMergeFields().getMMerge8())
  //                .orElse(target.getMergeFields().getMMerge8()),
  //            Optional.ofNullable(source.getMergeFields().getMMerge9())
  //                .orElse(target.getMergeFields().getMMerge9())));
  //  }

  private static LocalDate parseLocalDateNullable(String date) {
    return Optional.ofNullable(date)
        .map(d -> LocalDate.parse(d, DateTimeFormatter.ISO_DATE))
        .orElse(null);
  }

  private static LocalDateTime parseLocalDateTimeNullable(String date) {
    return Optional.ofNullable(date)
        .map(d -> LocalDateTime.parse(d, DateTimeFormatter.ISO_DATE_TIME))
        .orElse(null);
  }
}

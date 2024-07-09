package com.speakupcambridge.model.mapper;

import com.speakupcambridge.model.AirtablePerson;
import com.speakupcambridge.model.MailchimpPerson;
import com.speakupcambridge.model.Person;
import com.speakupcambridge.model.enums.ActiveStatus;
import com.speakupcambridge.model.enums.PersonType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

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
        airtablePerson.getFields().getReminderEmails().equalsIgnoreCase("yes"),
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
            person.getReminderEmails() ? "Yes" : "No",
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
        mailchimpPerson.getStatus().equalsIgnoreCase("subscribed"),
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
        person.getAirtableId(),
        person.getEmail(),
        null,
        null,
        person.getName(),
        null,
        null,
        person.getReminderEmails() ? "subscribed" : "unsubscribed",
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
            person.getType().toString(),
            person.getMembershipStatus().toString(),
            person.getDuesLastPaid().toString(),
            null,
            null));
  }

  private static LocalDate parseLocalDateNullable(String date) {
    if (Objects.isNull(date)) {
      return null;
    }
    return LocalDate.parse(date, DateTimeFormatter.ISO_DATE);
  }

  private static LocalDateTime parseLocalDateTimeNullable(String date) {
    if (Objects.isNull(date)) {
      return null;
    }
    return LocalDateTime.parse(date, DateTimeFormatter.ISO_DATE_TIME);
  }
}

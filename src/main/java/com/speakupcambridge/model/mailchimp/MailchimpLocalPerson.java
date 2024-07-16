package com.speakupcambridge.model.mailchimp;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Table(name = "mailchimp_prepared_persons_data")
public class MailchimpLocalPerson extends MailchimpPerson {
  public MailchimpLocalPerson(MailchimpPerson person) {
    super(person);
  }

  public MailchimpLocalPerson(
      String id,
      String email,
      String uniqueEmailId,
      String contactId,
      String fullName,
      String webId,
      String emailType,
      String status,
      String unsubscribeReason,
      String consentsToOneToOneMessaging,
      String smsPhoneNumber,
      String smsSubscriptionStatus,
      String smsSubscriptionLastUpdated,
      MailchimpPerson.MailchimpPersonMergeFields mergeFields) {
    super(
        id,
        email,
        uniqueEmailId,
        contactId,
        fullName,
        webId,
        emailType,
        status,
        unsubscribeReason,
        consentsToOneToOneMessaging,
        smsPhoneNumber,
        smsSubscriptionStatus,
        smsSubscriptionLastUpdated,
        mergeFields);
  }
}

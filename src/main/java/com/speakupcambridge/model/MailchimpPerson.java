package com.speakupcambridge.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record MailchimpPerson(
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
    String smsSubscriptionLastUpdated)
    implements MailchimpRecord {
  @JsonIgnoreProperties(ignoreUnknown = true)
  public record MailchimpPersonMergeFields(
      String fname,
      String lname,
      String address,
      String phone,
      String mmerge5,
      String mmerge6,
      String mmerge7,
      String mmerge8,
      String mmerge9) {}
}

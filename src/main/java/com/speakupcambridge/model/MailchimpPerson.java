package com.speakupcambridge.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Optional;

@MappedSuperclass
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class MailchimpPerson implements MailchimpRecord {
  private String id;

  @Id
  @JsonProperty("email_address")
  private String email;

  @JsonProperty("unique_email_id")
  private String uniqueEmailId;

  @JsonProperty("contact_id")
  private String contactId;

  @JsonProperty("full_name")
  private String fullName;

  @JsonProperty("web_id")
  private String webId;

  @JsonProperty("email_type")
  private String emailType;

  @JsonProperty("status")
  private String status;

  @JsonProperty("unsubscribe_reason")
  private String unsubscribeReason;

  @JsonProperty("consents_to_one_to_one_messaging")
  private String consentsToOneToOneMessaging;

  @JsonProperty("sms_phone_number")
  private String smsPhoneNumber;

  @JsonProperty("sms_subscription_status")
  private String smsSubscriptionStatus;

  @JsonProperty("sms_subscription_last_updated")
  private String smsSubscriptionLastUpdated;

  @JsonProperty("merge_fields")
  @Embedded
  private MailchimpPersonMergeFields mergeFields;

  @Embeddable
  @NoArgsConstructor
  @AllArgsConstructor
  @Getter
  @Setter
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class MailchimpPersonMergeFields {
    @JsonProperty("FNAME")
    private String fName;

    @JsonProperty("LNAME")
    private String lName;

    @JsonProperty("ADDRESS")
    private String address;

    @JsonProperty("PHONE")
    private String phone;

    @JsonProperty("MMERGE5")
    private String mMerge5;

    @JsonProperty("MMERGE6")
    private String mMerge6;

    @JsonProperty("MMERGE7")
    private String mMerge7;

    @JsonProperty("MMERGE8")
    private String mMerge8;

    @JsonProperty("MMERGE9")
    private String mMerge9;
  }

  public MailchimpPerson(MailchimpPerson person) {
    copyFrom(person);
  }

  public void overwriteWith(MailchimpPerson person, boolean overwriteWithNull) {
    if (overwriteWithNull) {
      copyFrom(person);
    } else {
      Optional.ofNullable(person.getId()).ifPresent(o -> this.id = o);
      Optional.ofNullable(person.getEmail()).ifPresent(o -> this.email = o);
      Optional.ofNullable(person.getUniqueEmailId()).ifPresent(o -> this.uniqueEmailId = o);
      Optional.ofNullable(person.getContactId()).ifPresent(o -> this.contactId = o);
      Optional.ofNullable(person.getFullName()).ifPresent(o -> this.fullName = o);
      Optional.ofNullable(person.getWebId()).ifPresent(o -> this.webId = o);
      Optional.ofNullable(person.getEmailType()).ifPresent(o -> this.emailType = o);
      Optional.ofNullable(person.getStatus()).ifPresent(o -> this.status = o);
      Optional.ofNullable(person.getUnsubscribeReason()).ifPresent(o -> this.unsubscribeReason = o);
      Optional.ofNullable(person.getConsentsToOneToOneMessaging())
          .ifPresent(o -> this.consentsToOneToOneMessaging = o);
      Optional.ofNullable(person.getSmsPhoneNumber()).ifPresent(o -> this.smsPhoneNumber = o);
      Optional.ofNullable(person.getSmsSubscriptionStatus())
          .ifPresent(o -> this.smsSubscriptionStatus = o);
      Optional.ofNullable(person.getSmsSubscriptionLastUpdated())
          .ifPresent(o -> this.smsSubscriptionLastUpdated = o);
      Optional.ofNullable(person.getMergeFields().getFName())
          .ifPresent(o -> this.mergeFields.fName = o);
      Optional.ofNullable(person.getMergeFields().getLName())
          .ifPresent(o -> this.mergeFields.lName = o);
      Optional.ofNullable(person.getMergeFields().getAddress())
          .ifPresent(o -> this.mergeFields.address = o);
      Optional.ofNullable(person.getMergeFields().getPhone())
          .ifPresent(o -> this.mergeFields.phone = o);
      Optional.ofNullable(person.getMergeFields().getMMerge5())
          .ifPresent(o -> this.mergeFields.mMerge5 = o);
      Optional.ofNullable(person.getMergeFields().getMMerge6())
          .ifPresent(o -> this.mergeFields.mMerge6 = o);
      Optional.ofNullable(person.getMergeFields().getMMerge7())
          .ifPresent(o -> this.mergeFields.mMerge7 = o);
      Optional.ofNullable(person.getMergeFields().getMMerge8())
          .ifPresent(o -> this.mergeFields.mMerge8 = o);
      Optional.ofNullable(person.getMergeFields().getMMerge9())
          .ifPresent(o -> this.mergeFields.mMerge9 = o);
    }
  }

  protected void copyFrom(MailchimpPerson person) {
    this.id = person.getId();
    this.email = person.getEmail();
    this.uniqueEmailId = person.getUniqueEmailId();
    this.contactId = person.getContactId();
    this.fullName = person.getFullName();
    this.webId = person.getWebId();
    this.emailType = person.getEmailType();
    this.status = person.getStatus();
    this.unsubscribeReason = person.getUnsubscribeReason();
    this.consentsToOneToOneMessaging = person.getConsentsToOneToOneMessaging();
    this.smsPhoneNumber = person.getSmsPhoneNumber();
    this.smsSubscriptionStatus = person.getSmsSubscriptionStatus();
    this.smsSubscriptionLastUpdated = person.getSmsSubscriptionLastUpdated();
    this.mergeFields = person.getMergeFields();
  }
}

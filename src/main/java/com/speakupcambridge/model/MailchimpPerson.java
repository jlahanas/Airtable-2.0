package com.speakupcambridge.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "mailchimp_persons_raw_data")
@JsonIgnoreProperties(ignoreUnknown = true)
public class MailchimpPerson implements MailchimpRecord {
  private String id;

  @Id
  @JsonProperty("email_address")
  private String email;

  @JsonProperty("unique_email_id")
  String uniqueEmailId;

  @JsonProperty("contact_id")
  String contactId;

  @JsonProperty("full_name")
  String fullName;

  @JsonProperty("web_id")
  String webId;

  @JsonProperty("email_type")
  String emailType;

  @JsonProperty("status")
  String status;

  @JsonProperty("unsubscribe_reason")
  String unsubscribeReason;

  @JsonProperty("consents_to_one_to_one_messaging")
  String consentsToOneToOneMessaging;

  @JsonProperty("sms_phone_number")
  String smsPhoneNumber;

  @JsonProperty("sms_subscription_status")
  String smsSubscriptionStatus;

  @JsonProperty("sms_subscription_last_updated")
  String smsSubscriptionLastUpdated;

  @Embedded MailchimpPersonMergeFields mergeFields;

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
}

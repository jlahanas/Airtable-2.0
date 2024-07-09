package com.speakupcambridge.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "airtable_persons_raw_data")
@JsonIgnoreProperties(ignoreUnknown = true)
public class AirtablePerson extends AirtableRecord {
  @Embedded private AirtablePersonFields fields;

  @JsonCreator
  public AirtablePerson(
      @JsonProperty("id") String id,
      @JsonProperty("createdTime") String createdTime,
      @JsonProperty("fields") AirtablePersonFields fields) {
    super(id, createdTime);
    this.fields = fields;
  }

  @JsonIgnoreProperties(ignoreUnknown = true)
  @Embeddable
  @NoArgsConstructor
  @AllArgsConstructor
  @Getter
  @Setter
  public static class AirtablePersonFields {
    @JsonProperty("First Name")
    private String firstName;

    @JsonProperty("Last Name")
    private String lastName;

    @JsonProperty("Name")
    private String name;

    @JsonProperty("Email Address")
    private String email;

    @JsonProperty("Address")
    private String address;

    @JsonProperty("Phone")
    private String phone;

    @JsonProperty("Type")
    private String type;

    @JsonProperty("Status")
    private String status;

    @JsonProperty("Attendance Status")
    private String attendanceStatus;

    @JsonProperty("Dues Last Paid")
    private String duesLastPaid;

    @JsonProperty("Dues")
    private String dues;

    @JsonProperty("Next Dues")
    private String nextDues;

    @JsonProperty("Reminder Emails")
    private String reminderEmails;

    @JsonProperty("Last Attended")
    private String lastAttendedDate;

    @JsonProperty("Last Speech")
    private String lastSpeechDate;

    @JsonProperty("TMI Membership")
    private String tmiMembership;

    @JsonProperty("TMI Status")
    private String tmiStatus;

    @JsonProperty("TMI Registration")
    private String tmiRegistration;

    @JsonProperty("Dues Credit")
    private String duesCredit;

    @JsonProperty("Dues Notes")
    private String duesNotes;
  }
}

package com.speakupcambridge.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record AirtablePerson(String id, String createdTime, AirtablePersonFields fields)
    implements AirtableRecord {
  @JsonIgnoreProperties(ignoreUnknown = true)
  public record AirtablePersonFields(
      @JsonProperty("First Name") String firstName,
      @JsonProperty("Last Name") String lastName,
      @JsonProperty("Name") String name,
      @JsonProperty("Email Address") String email,
      @JsonProperty("Address") String address,
      @JsonProperty("Phone") String phone,
      @JsonProperty("Type") String type,
      @JsonProperty("Status") String status,
      @JsonProperty("Attendance Status") String attendanceStatus,
      @JsonProperty("Dues Last Paid") String duesLastPaid,
      @JsonProperty("Dues") String dues,
      @JsonProperty("Next Dues") String nextDues,
      @JsonProperty("Reminder Emails") String reminderEmails,
      @JsonProperty("Last Attended") String lastAttendedDate,
      @JsonProperty("Last Speech") String lastSpeechDate,
      @JsonProperty("TMI Membership") String tmiMembership,
      @JsonProperty("TMI Status") String tmiStatus,
      @JsonProperty("TMI Registration") String tmiRegistration,
      @JsonProperty("Dues Credit") String duesCredit,
      @JsonProperty("Dues Notes") String duesNotes) {}
}

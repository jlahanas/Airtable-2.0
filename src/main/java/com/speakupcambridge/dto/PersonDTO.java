package com.speakupcambridge.dto;

import java.util.Date;

public record PersonDTO(
    String name,
    String firstName,
    String lastName,
    String type,
    String Status,
    String attendanceStatus,
    String emailAddress,
    Boolean reminderEmails,
    Date lastAttendedDate,
    Date lastRoleDate,
    String lastRole,
    Date lastSpeechDate,
    String lastSpeech,
    Date duesLastPaid,
    String phoneNumber,
    Date createdDateTime) {}

package com.speakupcambridge.model;

import com.speakupcambridge.model.enums.ActiveStatus;
import com.speakupcambridge.model.enums.PersonType;
import com.speakupcambridge.model.enums.RoleType;
import com.speakupcambridge.model.enums.SpeechType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "person")
public class Person {
  @Id @GeneratedValue private UUID id;

  private String airtableId;

  private String name;
  private String firstName;
  private String lastName;

  @Enumerated(EnumType.STRING)
  private PersonType type;

  @Enumerated(EnumType.STRING)
  private ActiveStatus membershipStatus;

  @Enumerated(EnumType.STRING)
  private ActiveStatus attendanceStatus;

  private String email;

  private String address;

  private Boolean reminderEmails;

  private LocalDate lastAttendedDate;

  private LocalDate lastRoleDate;

  @Enumerated(EnumType.STRING)
  private RoleType lastRole;

  private LocalDate lastSpeechDate;

  @Enumerated(EnumType.STRING)
  private SpeechType lastSpeech;

  private LocalDate duesLastPaid;

  private String phone;

  private LocalDateTime createdTime;

  @ElementCollection private List<String> meetingIds;

  @ElementCollection private List<String> roleMeetingIds;

  @ElementCollection private List<String> speechMeetingIds;
}

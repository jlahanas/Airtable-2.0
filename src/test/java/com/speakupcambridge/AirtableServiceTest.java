package com.speakupcambridge;

import com.speakupcambridge.component.AirtableJsonMapper;
import com.speakupcambridge.model.Person;
import com.speakupcambridge.model.airtable.AirtableDuesPeriod;
import com.speakupcambridge.model.airtable.AirtableMeeting;
import com.speakupcambridge.model.airtable.AirtablePerson;
import com.speakupcambridge.repository.airtable.*;
import com.speakupcambridge.service.AirtableService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
public class AirtableServiceTest {
  // This matches the ID in the JSON test files
  private static final String VALID_ID = "validId";
  private static final String PERSON_JSON_FILE_PATH =
      "src/test/resources/model/AirtablePerson.json";

  @Mock private RemoteAirtablePersonRepository remoteAirtablePersonRepository;
  @Mock private RemoteAirtableMeetingRepository remoteAirtableMeetingRepository;
  @Mock private RemoteAirtableDuesPeriodRepository remoteAirtableDuesPeriodRepository;
  @Mock private LocalAirtablePersonJpaRepository localAirtablePersonJpaRepository;
  @Mock private LocalAirtableMeetingJpaRepository localAirtableMeetingJpaRepository;
  @Mock private LocalAirtableDuesPeriodJpaRepository localAirtableDuesPeriodJpaRepository;
  private AirtableJsonMapper airtableJsonMapper;

  private AirtableService airtableService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);

    this.airtableService =
        new AirtableService(
            this.remoteAirtablePersonRepository,
            this.remoteAirtableMeetingRepository,
            this.remoteAirtableDuesPeriodRepository,
            this.localAirtablePersonJpaRepository,
            this.localAirtableMeetingJpaRepository,
            this.localAirtableDuesPeriodJpaRepository);
    this.airtableJsonMapper = new AirtableJsonMapper();
  }

  //  void setupRestCallMocks() {
  //    String personJson = readJsonFile(PERSON_JSON_FILE_PATH);
  //    String meetingJson = readJsonFile(MEETING_JSON_FILE_PATH);
  //    String duesJson = readJsonFile(DUES_JSON_FILE_PATH);
  //
  //    // Persons record(s)
  //    when(this.airtableRestService.fetchRecords(eq(PERSON_TABLE_NAME), anyString()))
  //        .thenAnswer(
  //            invocation -> {
  //              String id = invocation.getArgument(1);
  //              if (!id.equals(VALID_ID)) {
  //                // TODO: Throw exception here?
  //                return "";
  //              }
  //              return personJson;
  //            });
  //    when(this.airtableRestService.fetchRecords(PERSON_TABLE_NAME))
  //        .thenReturn(arrayWrapJsonRecord(personJson));
  //
  //    // Meetings record(s)
  //    when(this.airtableRestService.fetchRecords(eq(MEETING_TABLE_NAME), anyString()))
  //        .thenAnswer(
  //            invocation -> {
  //              String id = invocation.getArgument(1);
  //              if (!id.equals(VALID_ID)) {
  //                // TODO: Throw exception here?
  //                return "";
  //              }
  //              return meetingJson;
  //            });
  //    when(this.airtableRestService.fetchRecords(MEETING_TABLE_NAME))
  //        .thenReturn(arrayWrapJsonRecord(meetingJson));
  //
  //    // Meetings record(s)
  //    when(this.airtableRestService.fetchRecords(eq(DUES_TABLE_NAME), anyString()))
  //        .thenAnswer(
  //            invocation -> {
  //              String id = invocation.getArgument(1);
  //              if (!id.equals(VALID_ID)) {
  //                // TODO: Throw exception here?
  //                return "";
  //              }
  //              return duesJson;
  //            });
  //    when(this.airtableRestService.fetchRecords(DUES_TABLE_NAME))
  //        .thenReturn(arrayWrapJsonRecord(duesJson));
  //  }

  @Test
  void syncDatabase_fetchesAndWrites() {
    // GIVEN/WHEN the service syncs the database
    this.airtableService.syncDatabase();

    // THEN the remotes are fetched from and the locals are written to
    verify(this.remoteAirtablePersonRepository, times(1)).findAll();
    verify(this.remoteAirtableMeetingRepository, times(1)).findAll();
    verify(this.remoteAirtableDuesPeriodRepository, times(1)).findAll();
    verify(this.localAirtablePersonJpaRepository, times(1)).saveAll(anyIterable());
    verify(this.localAirtableMeetingJpaRepository, times(1)).saveAll(anyIterable());
    verify(this.localAirtableDuesPeriodJpaRepository, times(1)).saveAll(anyIterable());
  }

  @Test
  void generatePersonsList_setsMeetingIds() {
    // GIVEN a local AirtablePerson repository with a single person
    String personJson = readJsonFile(PERSON_JSON_FILE_PATH);

    List<AirtablePerson> airtablePersonList =
        this.airtableJsonMapper.mapList(arrayWrapJsonRecord(personJson), AirtablePerson.class);
    when(this.localAirtablePersonJpaRepository.findAll()).thenReturn(airtablePersonList);
    // ...and a mock meeting repository setup with three "meetings", with the person in
    // a role of each type, respectively:
    List<AirtableMeeting> airtableMeetingAttendeeList =
        this.airtableJsonMapper.mapList(
            arrayWrapJsonRecord(
                generateMeetingJsonWithSinglePersonRole(
                    "meeting0", "Attended", airtablePersonList.get(0).getId())),
            AirtableMeeting.class);
    when(this.localAirtableMeetingJpaRepository.findByAllAttendeesContaining(
            airtablePersonList.get(0).getId()))
        .thenReturn(airtableMeetingAttendeeList);
    List<AirtableMeeting> airtableMeetingRoleTakerList =
        this.airtableJsonMapper.mapList(
            arrayWrapJsonRecord(
                generateMeetingJsonWithSinglePersonRole(
                    "meeting1", "Toastmaster", airtablePersonList.get(0).getId())),
            AirtableMeeting.class);
    when(this.localAirtableMeetingJpaRepository.findByAllRoleTakersContaining(
            airtablePersonList.get(0).getId()))
        .thenReturn(airtableMeetingRoleTakerList);
    List<AirtableMeeting> airtableMeetingSpeechGiverList =
        this.airtableJsonMapper.mapList(
            arrayWrapJsonRecord(
                generateMeetingJsonWithSinglePersonRole(
                    "meeting2", "Speech", airtablePersonList.get(0).getId())),
            AirtableMeeting.class);
    when(this.localAirtableMeetingJpaRepository.findByAllSpeechGiversContaining(
            airtablePersonList.get(0).getId()))
        .thenReturn(airtableMeetingSpeechGiverList);

    // WHEN the service generates a person list
    List<Person> personList = this.airtableService.generatePersonsList();

    // THEN the person list has a person record
    assert personList.size() == 1;
    // ...and the person's list of attended meeting IDs has just meeting0
    assert personList.get(0).getMeetingIds().size() == 1;
    assert personList.get(0).getMeetingIds().contains("meeting0");
    // ...and the person's list of role meetings has just meeting1
    assert personList.get(0).getRoleMeetingIds().size() == 1;
    assert personList.get(0).getRoleMeetingIds().contains("meeting1");
    // ...and the person's list of speech meetings has just meeting2
    assert personList.get(0).getSpeechMeetingIds().size() == 1;
    assert personList.get(0).getSpeechMeetingIds().contains("meeting2");
  }

  @Test
  void returnsPersonRecord() {
    // GIVEN/WHEN the record with a valid ID is requested
    AirtablePerson airtablePerson = airtableService.fetchPerson(VALID_ID);

    // THEN the record is returned
    assert Objects.nonNull(airtablePerson);
    assert airtablePerson.getId().equals(VALID_ID);
  }

  @Test
  void returnsPersonRecordList() {
    // GIVEN/WHEN all records are requested
    List<AirtablePerson> airtablePersons = airtableService.fetchPersons();

    // THEN a list of records is returned, containing the record JSON
    assert Objects.nonNull(airtablePersons);
    assert !airtablePersons.isEmpty();
    assert airtablePersons.get(0).getId().equals(VALID_ID);
  }

  @Test
  void returnsMeetingRecord() {
    // GIVEN/WHEN the record with a valid ID is requested
    AirtableMeeting airtableMeeting = airtableService.fetchMeeting(VALID_ID);

    // THEN the record is returned
    assert Objects.nonNull(airtableMeeting);
    assert airtableMeeting.getId().equals(VALID_ID);
  }

  @Test
  void returnsMeetingRecordList() {
    // GIVEN/WHEN all records are requested
    List<AirtableMeeting> airtableMeetings = airtableService.fetchMeetings();

    // THEN a list of records is returned, containing the record JSON
    assert Objects.nonNull(airtableMeetings);
    assert !airtableMeetings.isEmpty();
    assert airtableMeetings.get(0).getId().equals(VALID_ID);
  }

  @Test
  void returnsDuesRecord() {
    // GIVEN/WHEN the record with a valid ID is requested
    AirtableDuesPeriod airtableDuesPeriod = airtableService.fetchDuesPeriod(VALID_ID);

    // THEN the record is returned
    assert Objects.nonNull(airtableDuesPeriod);
    assert airtableDuesPeriod.getId().equals(VALID_ID);
  }

  @Test
  void returnsDuesRecordList() {
    // GIVEN/WHEN all records are requested
    List<AirtableDuesPeriod> airtableDuesPeriods = airtableService.fetchDuesPeriods();

    // THEN a list of records is returned, containing the record JSON
    assert Objects.nonNull(airtableDuesPeriods);
    assert !airtableDuesPeriods.isEmpty();
    assert airtableDuesPeriods.get(0).getId().equals(VALID_ID);
  }

  private String readJsonFile(String filepath) {
    try {
      Path path = Paths.get(filepath);
      return Files.readString(path);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private String arrayWrapJsonRecord(String record) {
    return String.format("{\"records\":[%s]}", record);
  }

  private String generateMeetingJsonWithSinglePersonRole(
      String meetingId, String roleType, String personId) {
    return "{"
        + String.format("\"id\":\"%s\",", meetingId)
        + "\"createdTime\":\"2019-12-11T21:30:21.000Z\","
        + "\"fields\": {"
        + "\"Meeting Date\": \"2019-12-11\","
        + String.format("\"%s\": [ \"%s\" ]", roleType, personId)
        + "}"
        + "}";
  }
}

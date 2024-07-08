package com.speakupcambridge;

import com.speakupcambridge.model.AirtableDuesPeriod;
import com.speakupcambridge.model.AirtableMeeting;
import com.speakupcambridge.model.AirtablePerson;
import com.speakupcambridge.repository.LocalAirtablePersonJpaRepository;
import com.speakupcambridge.repository.RemoteAirtableDuesPeriodRepository;
import com.speakupcambridge.repository.RemoteAirtableMeetingRepository;
import com.speakupcambridge.repository.RemoteAirtablePersonRepository;
import com.speakupcambridge.service.AirtableRestService;
import com.speakupcambridge.service.AirtableService;
import com.speakupcambridge.component.JsonMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
public class AirtableServiceTest {
  private static final String PERSON_TABLE_NAME = "Persons";
  private static final String MEETING_TABLE_NAME = "Meetings";
  private static final String DUES_TABLE_NAME = "Dues";

  private static final String PERSON_JSON_FILE_PATH =
      "src/test/resources/model/AirtablePerson.json";
  private static final String MEETING_JSON_FILE_PATH =
      "src/test/resources/model/AirtableMeeting.json";
  private static final String DUES_JSON_FILE_PATH =
      "src/test/resources/model/AirtableDuesPeriod.json";
  // This matches the ID in the JSON test files
  private static final String VALID_ID = "ValidId";

  //  @Mock private AirtableRestService airtableRestService;
  //  private JsonMapper jsonMapper;
  // TODO: Replace these with mocks, lol
  @Autowired private RemoteAirtablePersonRepository remoteAirtablePersonRepository;
  @Autowired private RemoteAirtableMeetingRepository remoteAirtableMeetingRepository;
  @Autowired private RemoteAirtableDuesPeriodRepository remoteAirtableDuesPeriodRepository;
  @Autowired private LocalAirtablePersonJpaRepository localAirtablePersonJpaRepository;

  private AirtableService airtableService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);

    //    this.jsonMapper = new JsonMapper();

    this.airtableService =
        new AirtableService(
            this.remoteAirtablePersonRepository,
            this.remoteAirtableMeetingRepository,
            this.remoteAirtableDuesPeriodRepository);
    //            PERSON_TABLE_NAME,
    //            MEETING_TABLE_NAME,
    //            DUES_TABLE_NAME,
    //            airtableRestService,
    //            jsonMapper);

    //    setupRestCallMocks();
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
  void writesPersonsToDb() {
    List<AirtablePerson> records = this.airtableService.fetchPersons();
    localAirtablePersonJpaRepository.saveAll(records);
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
}

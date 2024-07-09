package com.speakupcambridge.service;

import com.speakupcambridge.model.*;
import com.speakupcambridge.model.mapper.PersonMapper;
import com.speakupcambridge.repository.*;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AirtableService {
  //  private final List<RemoteAirtableRepository<AirtableRecord>> repositories;

  private final RemoteAirtablePersonRepository remotePersonRepository;
  private final RemoteAirtableMeetingRepository remoteMeetingRepository;
  private final RemoteAirtableDuesPeriodRepository remoteDuesPeriodRepository;
  private final LocalAirtablePersonJpaRepository localRawPersonRepository;
  private final LocalAirtableMeetingJpaRepository localRawMeetingRepository;
  private final LocalAirtableDuesPeriodJpaRepository localRawDuesPeriodRepository;
  private final LocalPersonRepository localPersonRepository;

  //  // TODO: Using the generic repositories List<List> runs into issues when trying to
  //  // select one of the correct type since it involves down casting.  Might not be possible
  //  public AirtableService(List<RemoteAirtableRepository<AirtableRecord>> repositories) {
  //    this.repositories = repositories;
  //  }

  public AirtableService(
      RemoteAirtablePersonRepository remotePersonRepository,
      RemoteAirtableMeetingRepository remoteMeetingRepository,
      RemoteAirtableDuesPeriodRepository remoteDuesPeriodRepository,
      LocalAirtablePersonJpaRepository localRawPersonRepository,
      LocalAirtableMeetingJpaRepository localRawMeetingRepository,
      LocalAirtableDuesPeriodJpaRepository localRawDuesPeriodRepository,
      LocalPersonRepository localPersonRepository) {
    //    this.repositories = null;
    this.remotePersonRepository = remotePersonRepository;
    this.remoteMeetingRepository = remoteMeetingRepository;
    this.remoteDuesPeriodRepository = remoteDuesPeriodRepository;
    this.localRawPersonRepository = localRawPersonRepository;
    this.localRawMeetingRepository = localRawMeetingRepository;
    this.localRawDuesPeriodRepository = localRawDuesPeriodRepository;
    this.localPersonRepository = localPersonRepository;
  }

  //  public List<List<AirtableRecord>> fetchAll() {
  //    List<List<AirtableRecord>> results = new ArrayList<>();
  //    for (RemoteAirtableRepository<AirtableRecord> repository : this.repositories) {
  //      results.add(repository.findAll());
  //    }
  //    return results;
  //  }

  public void syncDatabase() {
    List<AirtablePerson> personList = this.fetchPersons();
    this.localRawPersonRepository.saveAll(personList);
    personList.clear();

    List<AirtableMeeting> meetingList = this.fetchMeetings();
    this.localRawMeetingRepository.saveAll(meetingList);
    meetingList.clear();

    List<AirtableDuesPeriod> duesPeriodList = this.fetchDuesPeriods();
    this.localRawDuesPeriodRepository.saveAll(duesPeriodList);
    duesPeriodList.clear();
  }

  public void generatePersonsTableFromRawData() {
    this.generatePersonsTableFromRawData(false);
  }

  public void generatePersonsTableFromRawData(boolean syncData) {
    if (syncData) {
      this.syncDatabase();
    }

    for (AirtablePerson airtablePerson : localRawPersonRepository.findAll()) {
      // One-to-one Map
      Person person = PersonMapper.toPerson(airtablePerson);

      // Enrich with the meeting data
      person.setMeetingIds(
          localRawMeetingRepository.findByAllAttendeesContaining(person.getAirtableId()).stream()
              .map(AirtableMeeting::getId)
              .collect(Collectors.toList()));
      person.setRoleMeetingIds(
          localRawMeetingRepository.findByAllRoleTakersContaining(person.getAirtableId()).stream()
              .map(AirtableMeeting::getId)
              .collect(Collectors.toList()));
      person.setSpeechMeetingIds(
          localRawMeetingRepository.findByAllSpeechGiversContaining(person.getAirtableId()).stream()
              .map(AirtableMeeting::getId)
              .collect(Collectors.toList()));

      // Write to database
      localPersonRepository.save(person);
    }
  }

  public AirtablePerson fetchPerson(@NonNull String personId) {
    return this.remotePersonRepository.findById(personId).orElse(null);
  }

  public List<AirtablePerson> fetchPersons() {
    return this.remotePersonRepository.findAll();
  }

  public AirtableMeeting fetchMeeting(@NonNull String meetingId) {
    return this.remoteMeetingRepository.findById(meetingId).orElse(null);
  }

  public List<AirtableMeeting> fetchMeetings() {
    return this.remoteMeetingRepository.findAll();
  }

  public AirtableDuesPeriod fetchDuesPeriod(@NonNull String duesPeriodId) {
    return this.remoteDuesPeriodRepository.findById(duesPeriodId).orElse(null);
  }

  public List<AirtableDuesPeriod> fetchDuesPeriods() {
    return this.remoteDuesPeriodRepository.findAll();
  }

  //  public void sync() {
  //    // Fetch all Airtable bases
  //    List<List<AirtableRecord>> records = this.fetchAll();
  //    List<AirtablePerson> persons = filterAndCastLists(records, AirtablePerson.class);
  //  }

  //  @SuppressWarnings("unchecked")
  //  public static <T extends AirtableRecord> List<List<T>> filterAndCastLists(
  //      List<List<? extends AirtableRecord>> records, Class<T> type) {
  //    return records.stream()
  //        .filter(list -> !list.isEmpty() && type.isInstance(list.get(0)))
  //        .map(list -> (List<T>) list)
  //        .collect(Collectors.toList());
  //  }
}

package com.speakupcambridge.service;

import com.speakupcambridge.model.AirtableDuesPeriod;
import com.speakupcambridge.model.AirtableMeeting;
import com.speakupcambridge.model.AirtablePerson;
import com.speakupcambridge.repository.ReadOnlyRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AirtableService {
  private final ReadOnlyRepository<AirtablePerson, String> remotePersonRepository;
  private final ReadOnlyRepository<AirtableMeeting, String> remoteMeetingRepository;
  private final ReadOnlyRepository<AirtableDuesPeriod, String> remoteDuesPeriodRepository;

  public AirtableService(
      ReadOnlyRepository<AirtablePerson, String> remotePersonRepository,
      ReadOnlyRepository<AirtableMeeting, String> remoteMeetingRepository,
      ReadOnlyRepository<AirtableDuesPeriod, String> remoteDuesPeriodRepository) {
    this.remotePersonRepository = remotePersonRepository;
    this.remoteMeetingRepository = remoteMeetingRepository;
    this.remoteDuesPeriodRepository = remoteDuesPeriodRepository;
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
}

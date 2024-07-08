package com.speakupcambridge.service;

import com.speakupcambridge.model.AirtableDuesPeriod;
import com.speakupcambridge.model.AirtableMeeting;
import com.speakupcambridge.model.AirtablePerson;
import com.speakupcambridge.repository.RemoteAirtableDuesPeriodRepository;
import com.speakupcambridge.repository.RemoteAirtableMeetingRepository;
import com.speakupcambridge.repository.RemoteAirtablePersonRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AirtableService {
  private final RemoteAirtablePersonRepository remotePersonRepository;
  private final RemoteAirtableMeetingRepository remoteMeetingRepository;
  private final RemoteAirtableDuesPeriodRepository remoteDuesPeriodRepository;

  public AirtableService(
      RemoteAirtablePersonRepository remotePersonRepository,
      RemoteAirtableMeetingRepository remoteMeetingRepository,
      RemoteAirtableDuesPeriodRepository remoteDuesPeriodRepository) {
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

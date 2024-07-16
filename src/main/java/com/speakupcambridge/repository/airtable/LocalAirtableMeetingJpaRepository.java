package com.speakupcambridge.repository.airtable;

import com.speakupcambridge.model.airtable.AirtableMeeting;

import java.util.List;

public interface LocalAirtableMeetingJpaRepository
    extends LocalAirtableJpaRepository<AirtableMeeting> {
  List<AirtableMeeting> findByFields_AttendedPersonIdsContaining(String personId);

  List<AirtableMeeting> findByFields_ToastmasterPersonIdsContaining(String personId);

  // The `AND` keyword is throwing off JPA
  //  @Query("SELECT m FROM AirtableMeeting m WHERE m.wordAndThoughtPersonIds LIKE ?1")
  //  List<AirtableMeeting> findByFields_WordAndThoughtPersonIdsContaining(String personId);

  List<AirtableMeeting> findByFields_HumoristPersonIdsContaining(String personId);

  List<AirtableMeeting> findByFields_RoundRobinPersonIdsContaining(String personId);

  List<AirtableMeeting> findByFields_RantPersonIdsContaining(String personId);

  List<AirtableMeeting> findByFields_SpeechPersonIdsContaining(String personId);

  List<AirtableMeeting> findByFields_TableTopicsLeaderPersonIdsContaining(String personId);

  List<AirtableMeeting> findByFields_TableTopicsPersonIdsContaining(String personId);

  List<AirtableMeeting> findByFields_GeneralEvaluatorPersonIdsContaining(String personId);

  List<AirtableMeeting> findByFields_SpeechEvaluatorPersonIdsContaining(String personId);

  List<AirtableMeeting> findByFields_TableTopicsEvaluatorPersonIdsContaining(String personId);

  List<AirtableMeeting> findByFields_TimerPersonIdsContaining(String personId);

  List<AirtableMeeting> findByFields_GrammarianPersonIdsContaining(String personId);

  List<AirtableMeeting> findByFields_AhUmCounterPersonIdsContaining(String personId);

  List<AirtableMeeting> findByAllAttendeesContaining(String personId);

  List<AirtableMeeting> findByAllSpeechGiversContaining(String personId);

  List<AirtableMeeting> findByAllRoleTakersContaining(String personId);
}

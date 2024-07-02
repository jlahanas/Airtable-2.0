package com.speakupcambridge.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record AirtableMeeting(String id, String createdTime, AirtableMeetingFields fields)
    implements AirtableRecord {
  @JsonIgnoreProperties(ignoreUnknown = true)
  public record AirtableMeetingFields(
      String meetingDate,
      List<String> attendedPersonIds,
      List<String> toastmasterPersonIds,
      List<String> wordAndThoughtPersonIds,
      List<String> humoristPersonIds,
      List<String> roundRobinPersonIds,
      List<String> rantPersonIds,
      List<String> speechPersonIds,
      List<String> tableTopicsLeaderPersonIds,
      List<String> tableTopicsPersonIds,
      List<String> generalEvaluatorPersonIds,
      List<String> speechEvaluatorPersonIds,
      List<String> tableTopicsEvaluatorPersonIds,
      List<String> timerPersonIds,
      List<String> grammarianPersonIds,
      List<String> ahUmCounterPersonIds) {}
}

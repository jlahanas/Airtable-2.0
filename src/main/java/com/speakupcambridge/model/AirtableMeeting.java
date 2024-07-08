package com.speakupcambridge.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "airtable_meetings_raw_data")
public class AirtableMeeting extends AirtableRecord {
  @Embedded private AirtableMeetingFields fields;

  @JsonIgnoreProperties(ignoreUnknown = true)
  @Embeddable
  @NoArgsConstructor
  @Getter
  @Setter
  public static class AirtableMeetingFields {
    private String meetingDate;
    private List<String> attendedPersonIds;
    private List<String> toastmasterPersonIds;
    private List<String> wordAndThoughtPersonIds;
    private List<String> humoristPersonIds;
    private List<String> roundRobinPersonIds;
    private List<String> rantPersonIds;
    private List<String> speechPersonIds;
    private List<String> tableTopicsLeaderPersonIds;
    private List<String> tableTopicsPersonIds;
    private List<String> generalEvaluatorPersonIds;
    private List<String> speechEvaluatorPersonIds;
    private List<String> tableTopicsEvaluatorPersonIds;
    private List<String> timerPersonIds;
    private List<String> grammarianPersonIds;
    private List<String> ahUmCounterPersonIds;
  }
}

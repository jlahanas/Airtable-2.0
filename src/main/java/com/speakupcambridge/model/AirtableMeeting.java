package com.speakupcambridge.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "airtable_meetings_raw_data")
public class AirtableMeeting extends AirtableRecord {
  @Embedded private AirtableMeetingFields fields;

  @ElementCollection @JsonIgnore private List<String> allAttendees;
  @ElementCollection @JsonIgnore private List<String> allSpeechGivers;
  @ElementCollection @JsonIgnore private List<String> allRoleTakers;

  @JsonCreator
  public AirtableMeeting(
      @JsonProperty("id") String id,
      @JsonProperty("createdTime") String createdTime,
      @JsonProperty("fields") AirtableMeetingFields fields) {
    super(id, createdTime);
    this.fields = fields;
    resolveAllAttendees();
    resolveAllSpeechGivers();
    resolveAllRoleTakers();
  }

  @JsonIgnoreProperties(ignoreUnknown = true)
  @Embeddable
  @NoArgsConstructor
  @AllArgsConstructor
  @Getter
  @Setter
  public static class AirtableMeetingFields {
    private String meetingDate;

    @ElementCollection
    @JsonProperty("Attended")
    private List<String> attendedPersonIds;

    @ElementCollection
    @JsonProperty("Toastmaster")
    private List<String> toastmasterPersonIds;

    @ElementCollection
    @JsonProperty("Word and Thought")
    private List<String> wordAndThoughtPersonIds;

    @ElementCollection
    @JsonProperty("Humorist")
    private List<String> humoristPersonIds;

    @ElementCollection
    @JsonProperty("Round Robin")
    private List<String> roundRobinPersonIds;

    @ElementCollection
    @JsonProperty("Rant")
    private List<String> rantPersonIds;

    @ElementCollection
    @JsonProperty("Speech")
    private List<String> speechPersonIds;

    @ElementCollection
    @JsonProperty("Table Topics Leader")
    private List<String> tableTopicsLeaderPersonIds;

    @ElementCollection
    @JsonProperty("Table Topics")
    private List<String> tableTopicsPersonIds;

    @ElementCollection
    @JsonProperty("General Evaluator")
    private List<String> generalEvaluatorPersonIds;

    @ElementCollection
    @JsonProperty("Speech Evaluator")
    private List<String> speechEvaluatorPersonIds;

    @ElementCollection
    @JsonProperty("Table Topics Evaluator")
    private List<String> tableTopicsEvaluatorPersonIds;

    @ElementCollection
    @JsonProperty("Timer")
    private List<String> timerPersonIds;

    @ElementCollection
    @JsonProperty("Grammarian")
    private List<String> grammarianPersonIds;

    @ElementCollection
    @JsonProperty("Ah-Um Counter")
    private List<String> ahUmCounterPersonIds;
  }

  private void resolveAllAttendees() {
    this.allAttendees =
        Stream.of(
                this.fields.attendedPersonIds,
                this.fields.toastmasterPersonIds,
                this.fields.wordAndThoughtPersonIds,
                this.fields.humoristPersonIds,
                this.fields.roundRobinPersonIds,
                this.fields.rantPersonIds,
                this.fields.speechPersonIds,
                this.fields.tableTopicsLeaderPersonIds,
                this.fields.tableTopicsPersonIds,
                this.fields.generalEvaluatorPersonIds,
                this.fields.speechEvaluatorPersonIds,
                this.fields.tableTopicsEvaluatorPersonIds,
                this.fields.timerPersonIds,
                this.fields.grammarianPersonIds,
                this.fields.ahUmCounterPersonIds)
            .filter(Objects::nonNull)
            .flatMap(List::stream)
            .distinct()
            .toList();
  }

  private void resolveAllSpeechGivers() {
    this.allSpeechGivers =
        Stream.of(
                this.fields.toastmasterPersonIds,
                this.fields.rantPersonIds,
                this.fields.speechPersonIds,
                this.fields.tableTopicsPersonIds)
            .filter(Objects::nonNull)
            .flatMap(List::stream)
            .distinct()
            .collect(Collectors.toList());
  }

  private void resolveAllRoleTakers() {
    this.allAttendees =
        Stream.of(
                this.fields.toastmasterPersonIds,
                this.fields.wordAndThoughtPersonIds,
                this.fields.humoristPersonIds,
                this.fields.roundRobinPersonIds,
                this.fields.tableTopicsLeaderPersonIds,
                this.fields.generalEvaluatorPersonIds,
                this.fields.speechEvaluatorPersonIds,
                this.fields.tableTopicsEvaluatorPersonIds,
                this.fields.timerPersonIds,
                this.fields.grammarianPersonIds,
                this.fields.ahUmCounterPersonIds)
            .filter(Objects::nonNull)
            .flatMap(List::stream)
            .distinct()
            .collect(Collectors.toList());
  }
}

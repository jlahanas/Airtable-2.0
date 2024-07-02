package com.speakupcambridge.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record AirtableDuesPeriod(String id, String createdTime, AirtableDuesPeriodFields fields)
    implements AirtableRecord {
  @JsonIgnoreProperties(ignoreUnknown = true)
  public record AirtableDuesPeriodFields(
      String duePeriod,
      String status,
      List<String> personIds,
      String start,
      String end,
      String nextCycle,
      String currentCycle,
      String total,
      String totalPaid) {}
}

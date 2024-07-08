package com.speakupcambridge.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "airtable_dues_raw_data")
public class AirtableDuesPeriod extends AirtableRecord {
  @Embedded private AirtableDuesPeriodFields fields;

  @JsonIgnoreProperties(ignoreUnknown = true)
  @Embeddable
  @NoArgsConstructor
  @Getter
  @Setter
  public static class AirtableDuesPeriodFields {
    private String duePeriod;
    private String status;
    private List<String> personIds;
    private String start;
    private String end;
    private String nextCycle;
    private String currentCycle;
    private String total;
    private String totalPaid;
  }
}

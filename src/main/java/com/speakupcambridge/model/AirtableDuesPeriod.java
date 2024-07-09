package com.speakupcambridge.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "airtable_dues_raw_data")
public class AirtableDuesPeriod extends AirtableRecord {
  @Embedded private AirtableDuesPeriodFields fields;

  @JsonCreator
  public AirtableDuesPeriod(
      @JsonProperty("id") String id,
      @JsonProperty("createdTime") String createdTime,
      @JsonProperty("fields") AirtableDuesPeriod.AirtableDuesPeriodFields fields) {
    super(id, createdTime);
    this.fields = fields;
  }

  @JsonIgnoreProperties(ignoreUnknown = true)
  @Embeddable
  @NoArgsConstructor
  @AllArgsConstructor
  @Getter
  @Setter
  public static class AirtableDuesPeriodFields {
    private String duePeriod;
    private String status;
    @ElementCollection private List<String> personIds;
    private String start;
    private String end;
    private String nextCycle;
    private String currentCycle;
    private String total;
    private String totalPaid;
  }
}

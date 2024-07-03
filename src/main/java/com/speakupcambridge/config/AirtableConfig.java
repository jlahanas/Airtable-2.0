package com.speakupcambridge.config;

import com.speakupcambridge.model.AirtableDuesPeriod;
import com.speakupcambridge.model.AirtableMeeting;
import com.speakupcambridge.model.AirtablePerson;
import com.speakupcambridge.model.AirtableRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;

@Component
@ConfigurationProperties
public class AirtableConfig {
  public final String baseUrl;
  public final String bearerToken;
  public final String baseId;
  public final String personsTable;
  public final String meetingsTable;
  public final String duesPeriodsTable;
  public final Map<Class<? extends AirtableRecord>, String> airtableClassTableMap;

  public AirtableConfig(
      @Value("${airtable.server.base_url}") String baseUrl,
      @Value("${airtable.api.token}") String bearerToken,
      @Value("${airtable.base.id}") String baseId,
      @Value("${airtable.tables.persons}") String personsTable,
      @Value("${airtable.tables.meetings}") String meetingsTable,
      @Value("${airtable.tables.duesPeriods}") String duesPeriodsTable) {
    this.baseUrl = baseUrl;
    this.bearerToken = bearerToken;
    this.baseId = baseId;
    this.personsTable = personsTable;
    this.meetingsTable = meetingsTable;
    this.duesPeriodsTable = duesPeriodsTable;
    this.airtableClassTableMap =
        Map.of(
            AirtablePerson.class,
            personsTable,
            AirtableMeeting.class,
            meetingsTable,
            AirtableDuesPeriod.class,
            duesPeriodsTable);
  }
}
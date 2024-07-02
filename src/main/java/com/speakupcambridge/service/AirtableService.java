package com.speakupcambridge.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.speakupcambridge.exceptions.UnexpectedJsonFormatException;
import com.speakupcambridge.model.AirtableDuesPeriod;
import com.speakupcambridge.model.AirtableMeeting;
import com.speakupcambridge.model.AirtablePerson;
import com.speakupcambridge.util.JsonMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class AirtableService {
  private final String personTableName;
  private final String meetingTableName;
  private final String duesPeriodTableName;

  private final AirtableRestService airtableRestService;

  private final JsonMapper jsonMapper;

  public AirtableService(
      @Value("${airtable.tables.persons}") String personTableName,
      @Value("${airtable.tables.meetings}") String meetingTableName,
      @Value("${airtable.tables.duesPeriods}") String duesPeriodTableName,
      AirtableRestService airtableRestService,
      JsonMapper jsonMapper) {
    this.personTableName = personTableName;
    this.meetingTableName = meetingTableName;
    this.duesPeriodTableName = duesPeriodTableName;

    this.airtableRestService = airtableRestService;

    this.jsonMapper = jsonMapper;
  }

  public AirtablePerson fetchPerson(@NonNull String personId) {
    String json = this.airtableRestService.fetchRecords(this.personTableName, personId);
    return this.jsonMapper.map(json, AirtablePerson.class);
  }

  public List<AirtablePerson> fetchPersons() {
    String json = this.airtableRestService.fetchRecords(this.personTableName);
    return this.jsonMapper.mapList(json, AirtablePerson.class);
  }

  public AirtableMeeting fetchMeeting(@NonNull String meetingId) {
    String json = this.airtableRestService.fetchRecords(this.meetingTableName, meetingId);
    return this.jsonMapper.map(json, AirtableMeeting.class);
  }

  public List<AirtableMeeting> fetchMeetings() {
    String json = this.airtableRestService.fetchRecords(this.meetingTableName);
    return this.jsonMapper.mapList(json, AirtableMeeting.class);
  }

  public AirtableDuesPeriod fetchDuesPeriod(@NonNull String duesPeriodId) {
    String json = this.airtableRestService.fetchRecords(this.duesPeriodTableName, duesPeriodId);
    return this.jsonMapper.map(json, AirtableDuesPeriod.class);
  }

  public List<AirtableDuesPeriod> fetchDuesPeriods() {
    String json = this.airtableRestService.fetchRecords(this.duesPeriodTableName);
    return this.jsonMapper.mapList(json, AirtableDuesPeriod.class);
  }

  //  private <T extends Record> T fetchRecord(
  //      @NonNull String tableName, @NonNull String recordId, @NonNull Class<T> toValueType) {
  //    String json = this.airtableRestService.fetchRecords(tableName, recordId);
  //
  //    try {
  //      JsonNode root = this.mapper.readTree(json);
  //      return mapper.convertValue(root, toValueType);
  //    } catch (JsonProcessingException e) {
  //      throw new UnexpectedJsonFormatException(e);
  //    }
  //  }
  //
  //  private <T extends Record> List<T> fetchRecords(
  //      @NonNull String tableName, @NonNull Class<T> toValueType) {
  //    String json = this.airtableRestService.fetchRecords(tableName);
  //
  //    List<T> entityList = new ArrayList<>();
  //
  //    JsonNode root;
  //    try {
  //      root = this.mapper.readTree(json);
  //    } catch (JsonProcessingException e) {
  //      throw new UnexpectedJsonFormatException(
  //          String.format(
  //              "Unexpected or malformed response to '%s' table GET request from Airtable",
  //              tableName),
  //          e);
  //    }
  //
  //    JsonNode records = root.get(RECORD_LIST_FIELD);
  //    if (records == null || !records.isArray()) {
  //      throw new UnexpectedJsonFormatException(
  //          String.format(
  //              "Missing or empty '%s' list in '%s' table GET response from Airtable",
  //              RECORD_LIST_FIELD, tableName));
  //    }
  //
  //    for (JsonNode record : records) {
  //      entityList.add(mapper.convertValue(record, toValueType));
  //    }
  //    return entityList;
  //  }
}

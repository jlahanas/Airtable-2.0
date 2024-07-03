package com.speakupcambridge.repository;

import com.speakupcambridge.exceptions.UnexpectedJsonFormatException;
import com.speakupcambridge.model.AirtableRecord;
import com.speakupcambridge.service.AirtableRestService;
import com.speakupcambridge.util.JsonMapper;

import java.util.List;
import java.util.Optional;

public abstract class RemoteAirtableRepository<T extends AirtableRecord>
    implements ReadOnlyRepository<T, String> {

  private final AirtableRestService airtableRestService;
  private final JsonMapper jsonMapper;
  private final String tableName;
  private final Class<T> entityType;

  public RemoteAirtableRepository(
      AirtableRestService airtableRestService,
      JsonMapper jsonMapper,
      String tableName,
      Class<T> entityType) {
    this.airtableRestService = airtableRestService;
    this.jsonMapper = jsonMapper;
    this.tableName = tableName;
    this.entityType = entityType;
  }

  @Override
  public Optional<T> findById(String s) {
    String json = this.airtableRestService.fetchRecords(this.tableName, s);
    try {
      return Optional.of(this.jsonMapper.map(json, entityType));
    } catch (UnexpectedJsonFormatException e) {
      return Optional.empty();
    }
  }

  @Override
  public List<T> findAll() {
    String json = this.airtableRestService.fetchRecords(this.tableName);
    return this.jsonMapper.mapList(json, this.entityType);
  }

  @Override
  public long count() {
    return ((List<T>) this.findAll()).size();
  }
}

package com.speakupcambridge.repository;

import com.speakupcambridge.exceptions.UnexpectedJsonFormatException;
import com.speakupcambridge.model.AirtableRecord;
import com.speakupcambridge.service.AirtableRestService;
import com.speakupcambridge.component.AirtableJsonMapper;
import lombok.Getter;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public abstract class RemoteAirtableRepository<T extends AirtableRecord>
    implements ReadOnlyRepository<T, String> {

  private final AirtableRestService airtableRestService;
  private final AirtableJsonMapper jsonMapper;
  @Getter private final String tableName;
  @Getter private final Class<T> entityType;

  public RemoteAirtableRepository(
      AirtableRestService airtableRestService,
      AirtableJsonMapper jsonMapper,
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
    List<T> records = this.jsonMapper.mapList(json, this.entityType);
    String offset = this.jsonMapper.getOffset(json);
    while (Objects.nonNull(offset) && !offset.isEmpty()) {
      json = this.airtableRestService.fetchRecords(this.tableName, offset);
      records.addAll(this.jsonMapper.mapList(json, this.entityType));
      offset = this.jsonMapper.getOffset(json);
    }

    return records;
  }

  @Override
  public long count() {
    return ((List<T>) this.findAll()).size();
  }
}

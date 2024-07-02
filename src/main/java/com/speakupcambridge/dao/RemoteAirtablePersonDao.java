package com.speakupcambridge.dao;

import com.speakupcambridge.model.AirtablePerson;
import com.speakupcambridge.service.AirtableRestService;
import com.speakupcambridge.util.JsonMapper;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Optional;
import java.util.List;

@Component
public class RemoteAirtablePersonDao implements ReadOnlyDao<AirtablePerson> {
  private final AirtableRestService airtableRestService;
  private final JsonMapper jsonMapper;

  public RemoteAirtablePersonDao(AirtableRestService airtableRestService, JsonMapper jsonMapper) {
    this.airtableRestService = airtableRestService;
    this.jsonMapper = jsonMapper;
  }

  public Optional<AirtablePerson> get(String id) {
    //    String json = this.airtableRestService.fetchRecords()
    return Optional.empty();
  }

  public List<AirtablePerson> getAll() {
    return Collections.emptyList();
  }
}

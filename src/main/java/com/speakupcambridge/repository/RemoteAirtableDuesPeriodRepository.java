package com.speakupcambridge.repository;

import com.speakupcambridge.config.AirtableConfig;
import com.speakupcambridge.model.AirtableDuesPeriod;
import com.speakupcambridge.service.AirtableRestService;
import com.speakupcambridge.component.JsonMapper;
import org.springframework.stereotype.Repository;

@Repository
public class RemoteAirtableDuesPeriodRepository
    extends RemoteAirtableRepository<AirtableDuesPeriod> {

  public RemoteAirtableDuesPeriodRepository(
      AirtableRestService airtableRestService,
      JsonMapper jsonMapper,
      AirtableConfig airtableConfig) {
    super(airtableRestService, jsonMapper, airtableConfig.meetingsTable, AirtableDuesPeriod.class);
  }
}

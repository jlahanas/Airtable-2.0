package com.speakupcambridge.repository.airtable;

import com.speakupcambridge.config.AirtableConfig;
import com.speakupcambridge.model.airtable.AirtableDuesPeriod;
import com.speakupcambridge.service.AirtableRestService;
import com.speakupcambridge.component.AirtableJsonMapper;
import org.springframework.stereotype.Repository;

@Repository
public class RemoteAirtableDuesPeriodRepository
    extends RemoteAirtableRepository<AirtableDuesPeriod> {

  public RemoteAirtableDuesPeriodRepository(
      AirtableRestService airtableRestService,
      AirtableJsonMapper airtableJsonMapper,
      AirtableConfig airtableConfig) {
    super(
        airtableRestService,
        airtableJsonMapper,
        airtableConfig.meetingsTable,
        AirtableDuesPeriod.class);
  }
}

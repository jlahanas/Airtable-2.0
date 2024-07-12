package com.speakupcambridge.repository;

import com.speakupcambridge.config.AirtableConfig;
import com.speakupcambridge.model.AirtableMeeting;
import com.speakupcambridge.service.AirtableRestService;
import com.speakupcambridge.component.AirtableJsonMapper;
import org.springframework.stereotype.Repository;

@Repository
public class RemoteAirtableMeetingRepository extends RemoteAirtableRepository<AirtableMeeting> {

  public RemoteAirtableMeetingRepository(
      AirtableRestService airtableRestService,
      AirtableJsonMapper jsonMapper,
      AirtableConfig airtableConfig) {
    super(airtableRestService, jsonMapper, airtableConfig.meetingsTable, AirtableMeeting.class);
  }
}

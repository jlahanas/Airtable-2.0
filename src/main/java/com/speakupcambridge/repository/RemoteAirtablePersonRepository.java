package com.speakupcambridge.repository;

import com.speakupcambridge.config.AirtableConfig;
import com.speakupcambridge.model.AirtablePerson;
import com.speakupcambridge.service.AirtableRestService;
import com.speakupcambridge.component.JsonMapper;
import org.springframework.stereotype.Repository;

@Repository
public class RemoteAirtablePersonRepository extends RemoteAirtableRepository<AirtablePerson> {

  public RemoteAirtablePersonRepository(
      AirtableRestService airtableRestService,
      JsonMapper jsonMapper,
      AirtableConfig airtableConfig) {
    super(airtableRestService, jsonMapper, airtableConfig.personsTable, AirtablePerson.class);
  }
}

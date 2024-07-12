package com.speakupcambridge.repository;

import com.speakupcambridge.config.AirtableConfig;
import com.speakupcambridge.model.AirtablePerson;
import com.speakupcambridge.service.AirtableRestService;
import com.speakupcambridge.component.AirtableJsonMapper;
import org.springframework.stereotype.Repository;

@Repository
public class RemoteAirtablePersonRepository extends RemoteAirtableRepository<AirtablePerson> {

  public RemoteAirtablePersonRepository(
      AirtableRestService airtableRestService,
      AirtableJsonMapper airtableJsonMapper,
      AirtableConfig airtableConfig) {
    super(
        airtableRestService, airtableJsonMapper, airtableConfig.personsTable, AirtablePerson.class);
  }
}

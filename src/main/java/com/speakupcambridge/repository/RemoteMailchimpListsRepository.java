package com.speakupcambridge.repository;

import com.speakupcambridge.component.MailchimpJsonMapper;
import com.speakupcambridge.service.MailchimpRestService;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RemoteMailchimpListsRepository {
  private final MailchimpRestService mailchimpRestService;
  private final MailchimpJsonMapper jsonMapper;

  public RemoteMailchimpListsRepository(
      MailchimpRestService mailchimpRestService, MailchimpJsonMapper jsonMapper) {
    this.mailchimpRestService = mailchimpRestService;
    this.jsonMapper = jsonMapper;
  }

  public List<String> findAllListIds() {
    String json = mailchimpRestService.fetchLists();
    return this.jsonMapper.getListIds(json);
  }
}

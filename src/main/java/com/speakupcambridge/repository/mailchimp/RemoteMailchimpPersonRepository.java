package com.speakupcambridge.repository.mailchimp;

import com.fasterxml.jackson.databind.JsonNode;
import com.speakupcambridge.component.MailchimpJsonMapper;
import com.speakupcambridge.model.mailchimp.MailchimpPerson;
import com.speakupcambridge.service.MailchimpRestService;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class RemoteMailchimpPersonRepository {
  private final MailchimpRestService mailchimpRestService;
  private final MailchimpJsonMapper jsonMapper;

  public RemoteMailchimpPersonRepository(
      MailchimpRestService mailchimpRestService, MailchimpJsonMapper jsonMapper) {
    this.mailchimpRestService = mailchimpRestService;
    this.jsonMapper = jsonMapper;
  }

  public MailchimpPerson findById(String id) {
    // TODO: Implement
    return new MailchimpPerson();
  }

  public MailchimpPerson findById(String id, String listId) {
    // TODO: Implement
    return new MailchimpPerson();
  }

  public List<MailchimpPerson> findAll() {
    List<MailchimpPerson> entityList = new ArrayList<>();
    String lists = this.mailchimpRestService.fetchLists();
    for (String listId : this.jsonMapper.getListIds(lists)) {
      entityList.addAll(this.findAll(listId));
    }
    return entityList;
  }

  public List<MailchimpPerson> findAll(String listId) {
    JsonNode root;

    // This fetches only the max entities per request members
    String members = this.mailchimpRestService.fetchMembers(listId);
    List<MailchimpPerson> entityList = new ArrayList<>(this.jsonMapper.getListMembers(members));
    int count = this.jsonMapper.getListSizeFromMembers(members);

    for (int offset = MailchimpRestService.MAX_ENTITIES_PER_REQUEST;
        offset < count;
        offset += MailchimpRestService.MAX_ENTITIES_PER_REQUEST) {
      members =
          this.mailchimpRestService.fetchMembers(
              listId, MailchimpRestService.MAX_ENTITIES_PER_REQUEST, offset);
      entityList.addAll(this.jsonMapper.getListMembers(members));
    }

    return entityList;
  }
}

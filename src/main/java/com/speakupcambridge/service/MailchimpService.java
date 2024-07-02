package com.speakupcambridge.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.speakupcambridge.exceptions.UnexpectedJsonFormatException;
import com.speakupcambridge.model.MailchimpPerson;
import org.springframework.http.HttpMethod;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MailchimpService {
  private static final String MEMBERS_LIST_FIELD = "members";

  private final MailchimpRestService mailchimpRestService;

  private final ObjectMapper mapper;

  public MailchimpService(MailchimpRestService mailchimpRestService, ObjectMapper mapper) {
    this.mailchimpRestService = mailchimpRestService;
    this.mapper = mapper;
  }

  public List<MailchimpPerson> fetchPersons(String listId) {
    // TODO: We may want to cache the list member count to avoid making two API calls when
    // mailchimpRestService.fetchLists() was already called
    int memberCount = getListMemberCount(listId);
    JsonNode root;

    if (memberCount < 0) {
      throw new UnexpectedJsonFormatException(
          String.format(
              "Unable to find member count of list '%s'. Either the list was not found or the JSON response schema has changed.",
              listId));
    }

    // One call's JSON is sufficient if the memberCount doesn't exceed the maximum
    if (memberCount <= MailchimpRestService.MAX_ENTITIES_PER_REQUEST) {
      String resp = this.mailchimpRestService.fetchMembers(listId);
      return this.mapJsonMembersListToMailchimpPersonList(resp);
    }

    // Otherwise, concatenate multiple lists
    List<MailchimpPerson> entityList = new ArrayList<>();
    for (int offset = 0;
        offset < memberCount;
        offset += MailchimpRestService.MAX_ENTITIES_PER_REQUEST) {
      String resp =
          this.mailchimpRestService.fetchMembers(
              listId, MailchimpRestService.MAX_ENTITIES_PER_REQUEST, offset);
      entityList.addAll(this.mapJsonMembersListToMailchimpPersonList(resp));
    }
    return entityList;
  }

  private int getListMemberCount(String listId) {
    JsonNode resp, lists;

    // Get the lists from Mailchimp and parse the JSON for the number of members in the
    // list with the given ID
    try {
      resp = this.mapper.readTree(this.mailchimpRestService.fetchLists());
    } catch (JsonProcessingException e) {
      throw new UnexpectedJsonFormatException(
          "Unexpected or malformed response to lists GET request from Mailchimp", e);
    }
    lists = resp.get("lists");
    if (lists == null || !lists.isArray()) {
      throw new UnexpectedJsonFormatException("Missing or empty 'lists' list in GET response");
    }

    for (JsonNode list : lists) {
      if (list.get("id").toString().equals(listId)) {
        return list.get("stats").get("member_count").asInt();
      }
    }
    return -1;
  }

  private List<MailchimpPerson> mapJsonMembersListToMailchimpPersonList(String json) {
    List<MailchimpPerson> entityList = new ArrayList<>();
    JsonNode root;

    try {
      root = this.mapper.readTree(json);
    } catch (JsonProcessingException e) {
      throw new UnexpectedJsonFormatException(
          String.format(
              "Unexpected or malformed response to '%s' list GET request from Mailchimp",
              MEMBERS_LIST_FIELD),
          e);
    }

    JsonNode records = root.get(MEMBERS_LIST_FIELD);
    if (records == null || !records.isArray()) {
      throw new UnexpectedJsonFormatException(
          String.format(
              "Missing or empty '%s' list in GET response from Airtable", MEMBERS_LIST_FIELD));
    }

    for (JsonNode record : records) {
      entityList.add(mapper.convertValue(record, MailchimpPerson.class));
    }
    return entityList;
  }
}

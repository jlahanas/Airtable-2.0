package com.speakupcambridge.component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.speakupcambridge.exceptions.UnexpectedJsonFormatException;
import com.speakupcambridge.model.mailchimp.MailchimpPerson;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MailchimpJsonMapper {
  public static final String LISTS_FIELD_NAME = "lists";
  public static final String MEMBERS_FIELD_NAME = "members";
  public static final String LIST_ID_FIELD_NAME = "id";
  public static final String LIST_STATS_FIELD_NAME = "stats";
  public static final String LIST_STATS_MEMBER_COUNT_FIELD_NAME = "member_count";
  public static final String MEMBERS_LIST_TOTAL_ITEMS_FIELD_NAME = "total_items";

  private final ObjectMapper mapper;

  public MailchimpJsonMapper() {
    this.mapper = new ObjectMapper();
  }

  public int getListMemberCountFromLists(String listJsonResp, String listId) {
    JsonNode lists = this.getListsNode(listJsonResp);
    for (JsonNode list : lists) {
      if (list.get(LIST_ID_FIELD_NAME).toString().equals(listId)) {
        return list.get(LIST_STATS_FIELD_NAME).get(LIST_STATS_MEMBER_COUNT_FIELD_NAME).asInt();
      }
    }
    return -1;
  }

  public List<String> getListIds(String listJsonResp) {
    JsonNode lists = this.getListsNode(listJsonResp);
    List<String> ids = new ArrayList<>();
    for (JsonNode list : lists) {
      ids.add(list.get(LIST_ID_FIELD_NAME).asText());
    }
    return ids;
  }

  public List<MailchimpPerson> getListMembers(String membersJsonResp) {
    List<MailchimpPerson> entityList = new ArrayList<>();
    JsonNode root;

    try {
      root = this.mapper.readTree(membersJsonResp);
    } catch (JsonProcessingException e) {
      throw new UnexpectedJsonFormatException(
          String.format("Unexpected or malformed '%s' Mailchimp JSON", MEMBERS_FIELD_NAME), e);
    }

    JsonNode records = root.get(MEMBERS_FIELD_NAME);
    if (records == null || !records.isArray()) {
      throw new UnexpectedJsonFormatException(
          String.format("Missing or empty '%s' list in Mailchimp json", MEMBERS_FIELD_NAME));
    }

    for (JsonNode record : records) {
      entityList.add(mapper.convertValue(record, MailchimpPerson.class));
    }
    return entityList;
  }

  public int getListSizeFromMembers(String membersJsonResp) {
    JsonNode root;

    try {
      root = this.mapper.readTree(membersJsonResp);
    } catch (JsonProcessingException e) {
      throw new UnexpectedJsonFormatException(
          String.format("Unexpected or malformed '%s' Mailchimp JSON", MEMBERS_FIELD_NAME), e);
    }

    JsonNode count = root.get(MEMBERS_LIST_TOTAL_ITEMS_FIELD_NAME);
    if (count == null || !count.isInt()) {
      throw new UnexpectedJsonFormatException(
          String.format(
              "Missing or invalid list length specifier '%s' in Mailchimp json",
              MEMBERS_LIST_TOTAL_ITEMS_FIELD_NAME));
    }

    return count.asInt();
  }

  private JsonNode getListsNode(String listJsonResp) {
    JsonNode root, lists;

    // Parse the JSON for the number of members in the list with the given ID
    try {
      root = this.mapper.readTree(listJsonResp);
    } catch (JsonProcessingException e) {
      throw new UnexpectedJsonFormatException("Unexpected or malformed Mailchimp lists JSON", e);
    }
    lists = root.get(LISTS_FIELD_NAME);
    if (lists == null || !lists.isArray()) {
      throw new UnexpectedJsonFormatException(
          "Missing or empty 'lists' list in Mailchimp lists JSON");
    }

    return lists;
  }
}

package com.speakupcambridge.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.speakupcambridge.exceptions.UnexpectedJsonFormatException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

@Service
public class MailchimpRestService {
  public static int MAX_ENTITIES_PER_REQUEST = 1000;
  private final String baseUrl;
  private final String serverId;
  private final String bearerToken;

  private final RestTemplate restTemplate;
  private final ObjectMapper mapper;

  public MailchimpRestService(
      @Value("${mailchimp.server.base_url}") String baseUrl,
      @Value("${mailchimp.server.id}") String serverId,
      @Value("${mailchimp.api.token}") String bearerToken,
      RestTemplate restTemplate) {
    this.baseUrl = baseUrl.replace("{mailchimp.server.id}", serverId);
    this.serverId = serverId;
    this.bearerToken = bearerToken;
    this.restTemplate = restTemplate;

    this.mapper = new ObjectMapper();
  }

  public String fetchLists() {
    String fullUrl = String.format("%s/lists", this.baseUrl);
    return restTemplate
        .exchange(fullUrl, HttpMethod.GET, buildAuthHeader(), String.class)
        .getBody();
  }

  public String fetchMembers(String listId) {
    String fullUrl =
        String.format("%s/members?count=%d", listUrl(listId), MAX_ENTITIES_PER_REQUEST);
    return this.restTemplate
        .exchange(fullUrl, HttpMethod.GET, buildAuthHeader(), String.class)
        .getBody();
  }

  public String fetchMembers(String listId, int count, int offset) {
    if (count > MAX_ENTITIES_PER_REQUEST) {
      throw new IllegalArgumentException(
          String.format(
              "Request for '%d' members exceeds Mailchimp limit of '%d'",
              count, MAX_ENTITIES_PER_REQUEST));
    }

    String fullUrl = String.format("%s/members?count=%d&offset=%d", listUrl(listId), count, offset);
    return this.restTemplate
        .exchange(fullUrl, HttpMethod.GET, buildAuthHeader(), String.class)
        .getBody();
  }

  private HttpEntity<String> buildAuthHeader() {
    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", String.format("Bearer %s", this.bearerToken));

    return new HttpEntity<>(headers);
  }

  private String listUrl(String listId) {
    return String.format("%s/lists/%s", this.baseUrl, listId);
  }

  private int getListMemberCount(String listId) {
    JsonNode resp, lists;

    // Get the lists from Mailchimp and parse the JSON for the number of members in the
    // list with the given ID
    try {
      resp = this.mapper.readTree(this.fetchLists());
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
}

package com.speakupcambridge.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

@Service
public class AirtableRestService {
  private final String baseUrl;
  private final String bearerToken;
  private final String baseId;

  private final RestTemplate restTemplate;

  public AirtableRestService(
      @Value("${airtable.server.base_url}") String baseUrl,
      @Value("${airtable.api.key}") String bearerToken,
      @Value("${airtable.base.id}") String baseId,
      RestTemplate restTemplate) {
    this.baseUrl = baseUrl;
    this.bearerToken = bearerToken;
    this.baseId = baseId;

    this.restTemplate = restTemplate;
  }

  public String fetchRecords(String tableName, String recordId) {
    return this.restTemplate
        .exchange(
            buildResourceUrl(tableName, recordId), HttpMethod.GET, buildAuthHeader(), String.class)
        .getBody();
  }

  public String fetchRecords(String tableName) {
    return fetchRecords(tableName, null);
  }

  private HttpEntity<String> buildAuthHeader() {
    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", String.format("Bearer %s", this.bearerToken));

    return new HttpEntity<>(headers);
  }

  private String buildResourceUrl(String tableName, String recordId) {
    String tableUrl =
        String.format("%s/%s/%s", this.baseUrl, this.baseId, formatUrlComponent(tableName));
    if (Objects.nonNull(recordId)) {
      tableUrl = String.format("%s/%s", tableUrl, recordId);
    }
    return tableUrl;
  }

  private static String formatUrlComponent(String urlComponent) {
    return URLEncoder.encode(urlComponent, StandardCharsets.UTF_8).replace(" ", "%20");
  }
}

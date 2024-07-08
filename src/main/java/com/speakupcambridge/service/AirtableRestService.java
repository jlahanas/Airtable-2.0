package com.speakupcambridge.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.Objects;

import com.speakupcambridge.util.RestUtils;

@Service
public class AirtableRestService {
  public static String OFFSET_PARAM = "offset";
  private final String baseUrl;
  private final String bearerToken;
  private final String baseId;

  //  private final RestTemplate restTemplate;
  //  private final RestClient restClient;
  private final HttpClient httpClient;

  public AirtableRestService(
      @Value("${airtable.server.base_url}") String baseUrl,
      @Value("${airtable.api.token}") String bearerToken,
      @Value("${airtable.base.id}") String baseId) {
    this.baseUrl = baseUrl;
    this.bearerToken = bearerToken;
    this.baseId = baseId;

    //    this.restTemplate = restTemplate;
    this.httpClient = HttpClient.newHttpClient();
  }

  public String fetchRecord(String tableName, String recordId) {
    return this.fetchRecordImpl(tableName, recordId, null);
  }

  public String fetchRecords(String tableName) {
    return this.fetchRecordImpl(tableName, null, null);
  }

  public String fetchRecords(String tableName, String offset) {
    return this.fetchRecordImpl(tableName, null, offset);
  }

  private HttpEntity<String> buildAuthHeader() {
    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(this.bearerToken);
    headers.set("Authorization", String.format("Bearer %s", this.bearerToken));
    //    headers.set("User-Agent", "PostmanRuntime/7.39.0");
    //    headers.set("Accept", "*/*");
    //    headers.set("Connection", "keep-alive");
    //    headers.set("Accept-Encoding", "gzip, deflate, br");
    //    headers.set("Host", "api.airtable.com");
    //    headers.set("Cache-Control", "no-cache");

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

  private String fetchRecordImpl(String tableName, String recordId, String offset) {
    String uri = buildResourceUrl(tableName, recordId);
    Map<String, String> queryParams = Objects.nonNull(offset) ? Map.of(OFFSET_PARAM, offset) : null;
    HttpResponse<String> response =
        RestUtils.submitGetRequest(this.httpClient, uri, this.bearerToken, queryParams);

    if (response.statusCode() == 404) {
      // 404 is only returned when a record isn't found
      // If a table is not found a 403 forbidden is returned
      return null;
    }

    if (response.statusCode() != 200) {
      String message =
          Objects.nonNull(recordId)
              ? String.format(
                  "Request for record '%s' failed with status %d: %s",
                  recordId, response.statusCode(), response.body())
              : String.format(
                  "Request for records from '%s' failed with status %d: %s",
                  tableName, response.statusCode(), response.body());
      throw new RuntimeException(message);
    }

    return response.body();
  }

  private static String formatUrlComponent(String urlComponent) {
    // Using URLEncoder.encode replaces %20 with %2520.  Need to find an alternative
    //    return URLEncoder.encode(urlComponent.replace(" ", "%20"), StandardCharsets.UTF_8);
    return urlComponent.replace(" ", "%20");
  }
}

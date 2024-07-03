package com.speakupcambridge.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Objects;

@Service
public class AirtableRestService {
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

  public String fetchRecords(String tableName, String recordId) {
    String uri = buildResourceUrl(tableName, recordId);
    HttpRequest request;
    HttpResponse<String> response;

    try {
      request =
          HttpRequest.newBuilder()
              .GET()
              .uri(new URI(uri))
              .header("Authorization", String.format("Bearer %s", this.bearerToken))
              .build();
    } catch (URISyntaxException e) {
      throw new RuntimeException(String.format("Invalid URI: '%s'", uri), e);
    }

    try {
      response = this.httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    } catch (IOException | InterruptedException e) {
      throw new RuntimeException(e);
    }

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

  //  public String fetchRecordsOld(String tableName, String recordId) {
  //    String url = buildResourceUrl(tableName, recordId);
  //    HttpEntity<String> header = buildAuthHeader();
  //    ResponseEntity<String> response =
  //        this.restTemplate.exchange(url, HttpMethod.GET, header, String.class);
  //
  //    System.out.println("Request URL: " + url);
  //    System.out.println("Request Headers: " + header.getHeaders());
  //    System.out.println("Response Status: " + response.getStatusCode());
  //    System.out.println("Response Body: " + response.getBody());
  //
  //    return response.getBody();
  //  }

  public String fetchRecords(String tableName) {
    return fetchRecords(tableName, null);
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

  private static String formatUrlComponent(String urlComponent) {
    // Using URLEncoder.encode replaces %20 with %2520.  Need to find an alternative
    //    return URLEncoder.encode(urlComponent.replace(" ", "%20"), StandardCharsets.UTF_8);
    return urlComponent.replace(" ", "%20");
  }
}

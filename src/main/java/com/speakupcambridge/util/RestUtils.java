package com.speakupcambridge.util;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class RestUtils {
  public static HttpResponse<String> submitGetRequest(
      HttpClient httpClient, String uri, String bearerToken) {
    HttpRequest request;
    HttpResponse<String> response;

    try {
      request =
          HttpRequest.newBuilder()
              .GET()
              .uri(new URI(uri))
              .header("Authorization", String.format("Bearer %s", bearerToken))
              .build();
    } catch (URISyntaxException e) {
      throw new RuntimeException(String.format("Invalid URI: '%s'", uri), e);
    }

    try {
      return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    } catch (IOException | InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  public static HttpResponse<String> submitGetRequest(
      HttpClient httpClient, String uri, String bearerToken, Map<String, String> queryParams) {
    if (Objects.isNull(queryParams) || queryParams.isEmpty()) {
      return submitGetRequest(httpClient, uri, bearerToken);
    }
    String newUri =
        String.format(
            "%s?%s",
            uri,
            queryParams.entrySet().stream()
                .map(entry -> String.format("%s=%s", entry.getKey(), entry.getValue()))
                .collect(Collectors.joining("&")));
    return submitGetRequest(httpClient, newUri, bearerToken);
  }
}

package com.speakupcambridge;

import com.speakupcambridge.service.AirtableRestService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(classes = {AirtableRestService.class, RestTemplate.class})
@TestPropertySource(locations = "classpath:application-test.properties")
public class AirtableRestServiceTest {
  private static String VALID_PERSON_ID = "rec0DRZ4UthW3X5h5";
  private static String INVALID_PERSON_ID = "0123";

  @Value("${airtable.tables.persons}")
  String personTableName;

  @Value("${airtable.server.base_url}")
  String baseUrl;

  @Value("${airtable.api.token}")
  String bearerToken;

  @Value("${airtable.base.id}")
  String baseId;

  private AirtableRestService airtableRestService;

  @BeforeEach
  void setUp() {
    RestTemplate restTemplate = new RestTemplate();
    this.airtableRestService = new AirtableRestService(baseUrl, bearerToken, baseId, restTemplate);
  }

  @Test
  void fetchRecords_returnsWhenValidId() {
    // GIVEN/WHEN fetching a record with a valid ID
    // THEN it executes successfully
    assert !this.airtableRestService.fetchRecords(personTableName, VALID_PERSON_ID).isEmpty();
  }

  @Test
  void fetchRecords_throwsExceptionWhenInvalidId() {
    // GIVEN/WHEN fetching a record with an invalid ID
    // THEN it throws a runtime exception
    assertThrows(
        RuntimeException.class,
        () -> {
          this.airtableRestService.fetchRecords(personTableName, INVALID_PERSON_ID);
        });
  }
}

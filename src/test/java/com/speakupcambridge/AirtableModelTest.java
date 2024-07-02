package com.speakupcambridge;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.speakupcambridge.model.AirtablePerson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public class AirtableModelTest {
  private ObjectMapper mapper;

  @BeforeEach
  void setUp() {
    this.mapper = new ObjectMapper();
  }

  @Test
  void airtablePersonJsonMapsToClass() {
    // GIVEN a nested JSON structure
    String jsonFilePath = "src/test/resources/model/AirtablePerson.json";
    String jsonString = null;
    AirtablePerson airtablePerson = null;
    try {
      jsonString = readJsonFile(jsonFilePath);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    // WHEN it's mapped to the AirtablePerson class
    try {
      airtablePerson = this.mapper.readValue(jsonString, AirtablePerson.class);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }

    // THEN it maps correctly
    assert Objects.nonNull(airtablePerson);
    assert Objects.nonNull(airtablePerson.createdTime());
    assert Objects.nonNull(airtablePerson.id());
    assert Objects.nonNull(airtablePerson.fields().firstName());
  }

  private String readJsonFile(String filePath) throws IOException {
    Path path = Paths.get(filePath);
    return Files.readString(path);
  }
}

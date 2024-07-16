package com.speakupcambridge.component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.speakupcambridge.exceptions.UnexpectedJsonFormatException;
import com.speakupcambridge.model.airtable.AirtableRecord;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Component
public class AirtableJsonMapper {
  public static String RECORD_LIST_FIELD = "records";
  public static String OFFSET_FIELD = "offset";
  private final ObjectMapper mapper;

  public AirtableJsonMapper() {
    this.mapper = new ObjectMapper();
  }

  public <T extends AirtableRecord> T map(String json, @NonNull Class<T> toValueType) {
    if (Objects.isNull(json)) {
      return null;
    }

    try {
      JsonNode root = this.mapper.readTree(json);
      return this.mapper.convertValue(root, toValueType);
    } catch (JsonProcessingException e) {
      throw new UnexpectedJsonFormatException(e);
    }
  }

  public <T extends AirtableRecord> List<T> mapList(String json, @NonNull Class<T> toValueType) {
    if (Objects.isNull(json)) {
      return Collections.emptyList();
    }

    List<T> entityList = new ArrayList<>();

    JsonNode root;
    try {
      root = this.mapper.readTree(json);
    } catch (JsonProcessingException e) {
      throw new UnexpectedJsonFormatException(e);
    }

    JsonNode records = root.get(RECORD_LIST_FIELD);
    if (records == null || !records.isArray()) {
      throw new UnexpectedJsonFormatException(
          String.format("Missing or empty '%s' list in JSON", RECORD_LIST_FIELD));
    }

    for (JsonNode record : records) {
      entityList.add(this.mapper.convertValue(record, toValueType));
    }
    return entityList;
  }

  public String getOffset(String json) {
    JsonNode root;
    try {
      root = this.mapper.readTree(json);
    } catch (JsonProcessingException e) {
      throw new UnexpectedJsonFormatException(e);
    }

    JsonNode offsetNode = root.get(OFFSET_FIELD);
    return Objects.nonNull(offsetNode) ? offsetNode.asText() : null;
  }
}

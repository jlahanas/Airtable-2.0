package com.speakupcambridge.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.speakupcambridge.exceptions.UnexpectedJsonFormatException;
import com.speakupcambridge.model.AirtableRecord;
import org.springframework.lang.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class JsonMapper {
  public static String RECORD_LIST_FIELD = "records";
  private final ObjectMapper mapper;

  public JsonMapper() {
    this.mapper = new ObjectMapper();
  }

  public <T extends AirtableRecord> T map(String json, @NonNull Class<T> toValueType) {
    if (Objects.isNull(json)) {
      return null;
    }

    try {
      JsonNode root = this.mapper.readTree(json);
      return mapper.convertValue(root, toValueType);
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
      entityList.add(mapper.convertValue(record, toValueType));
    }
    return entityList;
  }
}

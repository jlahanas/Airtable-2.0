package com.speakupcambridge.model.enums;

import java.util.Arrays;
import java.util.EnumSet;

public enum RoleType {
  TOASTMASTER("Toastmaster"),
  WORD_AND_THOUGHT("Word and Thought"),
  TIMER("Timer"),
  HUMORIST("Humorist"),
  AH_UM_COUNTER("Ah-Um Counter"),
  ROUND_ROBIN("Round Robin"),
  TABLE_TOPICS_LEADER("Table Topics Leader"),
  GENERAL_EVALUATOR("General Evaluator"),
  SPEECH_EVALUATOR("Speech Evaluator"),
  TABLE_TOPICS_EVALUATOR("Table Topics Evaluator"),
  GRAMMARIAN("Grammarian");

  private final String displayValue;

  RoleType(String displayValue) {
    this.displayValue = displayValue;
  }

  @Override
  public String toString() {
    return displayValue;
  }

  public static RoleType fromString(String s) {
    return Arrays.stream(RoleType.values())
        .filter(type -> type.toString().equals(s))
        .findFirst()
        .orElseThrow(
            () ->
                new IllegalArgumentException(
                    String.format(
                        "No matching %s enumeration for '%s'", RoleType.class.toString(), s)));
  }
}

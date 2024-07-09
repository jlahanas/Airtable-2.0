package com.speakupcambridge.model.enums;

import java.util.Arrays;

public enum SpeechType {
  SPEECH("Speech"),
  TABLE_TOPICS("Table Topics"),
  RANT("Rant");

  private final String displayValue;

  SpeechType(String displayValue) {
    this.displayValue = displayValue;
  }

  @Override
  public String toString() {
    return displayValue;
  }

  public static SpeechType fromString(String s) {
    return Arrays.stream(SpeechType.values())
        .filter(type -> type.toString().equals(s))
        .findFirst()
        .orElseThrow(
            () ->
                new IllegalArgumentException(
                    String.format(
                        "No matching %s enumeration for '%s'", SpeechType.class.toString(), s)));
  }
}

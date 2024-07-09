package com.speakupcambridge.model.enums;

import java.util.Arrays;

public enum PersonType {
  OFFICER("Member/Officer"),
  MEMBER("Member"),
  GUEST("Guest");

  private final String displayValue;

  PersonType(String displayValue) {
    this.displayValue = displayValue;
  }

  @Override
  public String toString() {
    return displayValue;
  }

  public static PersonType fromString(String s) {
    return Arrays.stream(PersonType.values())
        .filter(type -> type.toString().equals(s))
        .findFirst()
        .orElseThrow(
            () ->
                new IllegalArgumentException(
                    String.format(
                        "No matching %s enumeration for '%s'", PersonType.class.toString(), s)));
  }
}

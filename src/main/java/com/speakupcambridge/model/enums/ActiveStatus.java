package com.speakupcambridge.model.enums;

import java.util.Arrays;

public enum ActiveStatus {
  ACTIVE("Active"),
  INACTIVE("Inactive");

  private final String displayValue;

  ActiveStatus(String displayValue) {
    this.displayValue = displayValue;
  }

  @Override
  public String toString() {
    return displayValue;
  }

  public static ActiveStatus fromString(String s) {
    return Arrays.stream(ActiveStatus.values())
        .filter(type -> type.toString().equals(s))
        .findFirst()
        .orElseThrow(
            () ->
                new IllegalArgumentException(
                    String.format(
                        "No matching %s enumeration for '%s'", ActiveStatus.class.toString(), s)));
  }
}

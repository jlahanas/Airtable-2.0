package com.speakupcambridge.model.enums;

import java.util.Arrays;

public enum SubscriptionStatus {
  SUBSCRIBED("subscribed"),
  UNSUBSCRIBED("unsubscribed"),
  CLEANED("cleaned");

  private final String displayValue;

  SubscriptionStatus(String displayValue) {
    this.displayValue = displayValue;
  }

  @Override
  public String toString() {
    return this.displayValue;
  }

  public static SubscriptionStatus fromString(String s) {
    return Arrays.stream(SubscriptionStatus.values())
        .filter(type -> type.toString().equals(s))
        .findFirst()
        .orElseThrow(
            () ->
                new IllegalArgumentException(
                    String.format(
                        "No matching %s enumeration for '%s'",
                        SubscriptionStatus.class.toString(), s)));
  }

  public static SubscriptionStatus fromBool(boolean b) {
    return b ? SUBSCRIBED : UNSUBSCRIBED;
  }
}

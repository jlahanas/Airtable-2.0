package com.speakupcambridge.model.enums;

public enum NullableBoolString {
  Yes("Yes"),
  No("No"),
  Unset("");

  private final String displayValue;

  NullableBoolString(String displayValue) {
    this.displayValue = displayValue;
  }

  @Override
  public String toString() {
    return this.displayValue;
  }

  public static NullableBoolString fromString(String s) {
    if (s.equalsIgnoreCase(Yes.toString())) {
      return Yes;
    }
    if (s.equalsIgnoreCase(No.toString())) {
      return No;
    }
    return Unset;
  }

  public boolean toBoolean() {
    return displayValue.equalsIgnoreCase(Yes.toString());
  }

  public static NullableBoolString fromBoolean(boolean b) {
    return b ? Yes : No;
  }
}

package com.speakupcambridge.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Entity
public class Person {
  private String name;
  private String firstName;
  private String lastName;

  @Enumerated(EnumType.STRING)
  private String type;
}

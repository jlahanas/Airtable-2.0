package com.speakupcambridge.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;

@Entity
public class Person {
  @Id private String name;
  private String firstName;
  private String lastName;

  private String type;
}

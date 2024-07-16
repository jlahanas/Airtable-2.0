package com.speakupcambridge.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.speakupcambridge.enums.DataActionType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CrudActionLog {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private DataActionType action;

  private String targetId;

  private String targetName;

  @JsonIgnore private String entityClass;

  @JsonIgnore private String repositoryClass;

  private String description;

  public CrudActionLog(
      DataActionType action,
      String targetId,
      String targetName,
      String entityClass,
      String repositoryClass,
      String description) {
    this.action = action;
    this.targetId = targetId;
    this.targetName = targetName;
    this.entityClass = entityClass;
    this.repositoryClass = repositoryClass;
    this.description = description;
  }
}

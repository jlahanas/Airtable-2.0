package com.speakupcambridge.entity;

import com.speakupcambridge.enums.DataActionType;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class MailchimpUpdateLog extends CrudActionLog {
  public MailchimpUpdateLog(
      DataActionType action,
      String targetId,
      String targetName,
      String entityClass,
      String repositoryClass,
      String description) {
    super(action, targetId, targetName, entityClass, repositoryClass, description);
  }
}

package com.speakupcambridge.event;

import com.speakupcambridge.enums.DataActionType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class CrudAction extends ApplicationEvent {
  private Object source;
  private DataActionType actionType;
  private Object targetId;
  private String targetName;
  private Class<?> entityType;
  private Class<?> repositoryType;
  private String description;

  public CrudAction(Object source) {
    super(source);
  }

  public CrudAction(
      Object source,
      DataActionType actionType,
      Object targetId,
      String targetName,
      Class<?> entityType,
      Class<?> repositoryType,
      String description) {
    super(source);
    this.actionType = actionType;
    this.targetId = targetId;
    this.targetName = targetName;
    this.entityType = entityType;
    this.repositoryType = repositoryType;
    this.description = description;
  }
}

package com.speakupcambridge.dao;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ReadOnlyDao<T> extends Dao<T> {
  @Override
  Optional<T> get(String id);

  @Override
  List<T> getAll();

  default void save(T t) {
    throw new UnsupportedOperationException("Save not permitted in read-only context");
  }

  default void update(T t, Map<String, Object> params) {
    throw new UnsupportedOperationException("Update not permitted in read-only context");
  }

  default void delete(T t) {
    throw new UnsupportedOperationException("Delete not permitted in read-only context");
  }
}

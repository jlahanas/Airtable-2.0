package com.speakupcambridge.dao;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface Dao<T> {
  Optional<T> get(String id);

  List<T> getAll();

  void save(T t);

  void update(T t, Map<String, Object> params);

  void delete(T t);
}

package com.speakupcambridge.repository;

import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

public interface ReadOnlyRepository<T, ID> extends Repository<T, ID> {
  Optional<T> findById(ID id);

  List<T> findAll();

  long count();

  default <S extends T> S save(S entity) {
    throw new UnsupportedOperationException("Save operation is not supported");
  }

  default void deleteById(ID id) {
    throw new UnsupportedOperationException("Delete operation is not supported");
  }

  default void delete(T entity) {
    throw new UnsupportedOperationException("Delete operation is not supported");
  }

  default void deleteAll(Iterable<? extends T> entities) {
    throw new UnsupportedOperationException("Delete operation is not supported");
  }

  default void deleteAll() {
    throw new UnsupportedOperationException("Delete operation is not supported");
  }
}

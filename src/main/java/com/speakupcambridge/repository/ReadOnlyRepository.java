package com.speakupcambridge.repository;

import org.springframework.data.repository.Repository;
import org.yaml.snakeyaml.events.Event;

public interface ReadOnlyRepository<T, ID> extends Repository<T, ID> {}

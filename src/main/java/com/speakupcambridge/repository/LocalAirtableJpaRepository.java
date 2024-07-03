package com.speakupcambridge.repository;

import com.speakupcambridge.model.AirtableRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocalAirtableJpaRepository<T extends AirtableRecord>
    extends JpaRepository<T, String> {}

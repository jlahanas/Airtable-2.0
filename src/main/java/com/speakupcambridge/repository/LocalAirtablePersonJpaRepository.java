package com.speakupcambridge.repository;

import com.speakupcambridge.model.AirtablePerson;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocalAirtablePersonJpaRepository extends JpaRepository<AirtablePerson, String> {}

package com.speakupcambridge.repository;

import com.speakupcambridge.model.AirtableDuesPeriod;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocalAirtableDuesPeriodJpaRepository
    extends JpaRepository<AirtableDuesPeriod, String> {}

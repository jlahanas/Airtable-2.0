package com.speakupcambridge.repository;

import com.speakupcambridge.model.AirtableMeeting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocalAirtableMeetingJpaRepository extends JpaRepository<AirtableMeeting, String> {}

package com.speakupcambridge.repository;

import com.speakupcambridge.model.MailchimpLocalPerson;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocalMailchimpPersonJpaRepository
    extends JpaRepository<MailchimpLocalPerson, String> {}

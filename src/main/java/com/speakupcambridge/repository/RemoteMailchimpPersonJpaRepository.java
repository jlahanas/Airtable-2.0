package com.speakupcambridge.repository;

import com.speakupcambridge.model.MailchimpRemotePerson;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RemoteMailchimpPersonJpaRepository
    extends JpaRepository<MailchimpRemotePerson, String> {}

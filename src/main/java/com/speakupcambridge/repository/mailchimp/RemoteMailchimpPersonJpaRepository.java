package com.speakupcambridge.repository.mailchimp;

import com.speakupcambridge.model.mailchimp.MailchimpRemotePerson;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RemoteMailchimpPersonJpaRepository
    extends JpaRepository<MailchimpRemotePerson, String> {

  Optional<MailchimpRemotePerson> findByEmail(String email);
}

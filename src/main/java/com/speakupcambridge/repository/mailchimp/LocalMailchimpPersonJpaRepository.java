package com.speakupcambridge.repository.mailchimp;

import com.speakupcambridge.model.mailchimp.MailchimpLocalPerson;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LocalMailchimpPersonJpaRepository
    extends JpaRepository<MailchimpLocalPerson, String> {

  Optional<MailchimpLocalPerson> findByEmail(String email);
}

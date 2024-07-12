package com.speakupcambridge.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "mailchimp_persons_raw_data")
public class MailchimpRemotePerson extends MailchimpPerson {}

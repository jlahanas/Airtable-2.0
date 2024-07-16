package com.speakupcambridge.controller;

import com.speakupcambridge.entity.MailchimpUpdateLog;
import com.speakupcambridge.model.Person;
import com.speakupcambridge.repository.LocalPersonRepository;
import com.speakupcambridge.service.AirtableService;
import com.speakupcambridge.service.MailchimpService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/persons")
public class PersonController {
  private final MailchimpService mailchimpService;
  private final AirtableService airtableService;
  private final LocalPersonRepository localPersonRepository;

  public PersonController(
      MailchimpService mailchimpService,
      AirtableService airtableService,
      LocalPersonRepository localPersonRepository) {
    this.mailchimpService = mailchimpService;
    this.airtableService = airtableService;
    this.localPersonRepository = localPersonRepository;
  }

  @GetMapping
  public List<Person> getAllPersons() {
    return this.localPersonRepository.findAll();
  }

  @GetMapping("/{id}")
  public Person getPerson(@PathVariable String id) {
    return this.localPersonRepository.findById(id).orElse(null);
  }

  @PostMapping("/sync")
  public List<Person> syncDatabase() {
    this.airtableService.syncDatabase();
    this.mailchimpService.syncDatabase();

    // This could and should happen in another service component, but for now since it's just a
    // direct get/save without any logic, keeping it here
    List<Person> personList = this.airtableService.generatePersonsList();
    this.localPersonRepository.saveAll(personList);

    return personList;
  }

  @GetMapping("/changes")
  public List<MailchimpUpdateLog> getPendingMailchimpChanges() {
    return this.mailchimpService.getAllChanges();
  }

  @PostMapping("/prepare-mailchimp")
  public void prepareMailchimp() {
    this.mailchimpService.generateUpdatedLocalTableFromPersonsList(
        this.localPersonRepository.findAll());
  }
}

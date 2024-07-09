package com.speakupcambridge.repository;

import com.speakupcambridge.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocalPersonRepository extends JpaRepository<Person, String> {}

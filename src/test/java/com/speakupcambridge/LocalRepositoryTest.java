package com.speakupcambridge;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.speakupcambridge.model.airtable.AirtablePerson;
import com.speakupcambridge.repository.airtable.LocalAirtableJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import testUtils.FileUtils;

import java.io.IOException;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@TestPropertySource(locations = "classpath:application-test.properties")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class LocalRepositoryTest {
  @Autowired private LocalAirtableJpaRepository<AirtablePerson> repository;
  private AirtablePerson testAirtablePerson;

  @BeforeEach
  void setUp() {
    ObjectMapper mapper = new ObjectMapper();
    String jsonFilePath = "src/test/resources/model/AirtablePerson.json";
    String jsonString = null;
    try {
      jsonString = FileUtils.readJsonFile(jsonFilePath);
      this.testAirtablePerson = mapper.readValue(jsonString, AirtablePerson.class);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  void canWriteAndDeleteRecordFromDb() {
    // GIVEN/WHEN an AirtablePerson record is saved to the DB, and deleted,
    // THEN the record is there when saved, and no longer there when deleted

    // WHEN the record is queried
    // THEN it should not be present initially
    assert this.repository.findById(this.testAirtablePerson.getId()).isEmpty();

    // WHEN the record is saved
    this.repository.save(this.testAirtablePerson);
    // THEN the record is present
    assert this.repository.findById(this.testAirtablePerson.getId()).isPresent();

    // WHEN the record is deleted
    this.repository.delete(this.testAirtablePerson);
    // THEN it should again not be present
    assert this.repository.findById(this.testAirtablePerson.getId()).isEmpty();
  }
}

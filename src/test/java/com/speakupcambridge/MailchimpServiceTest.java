package com.speakupcambridge;

import com.speakupcambridge.service.MailchimpService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
public class MailchimpServiceTest {
  @Autowired private MailchimpService mailchimpService;

  @Test
  void generateTableFromRawData_generatesTable() {
    this.mailchimpService.generateTableFromRawData(true);
  }
}

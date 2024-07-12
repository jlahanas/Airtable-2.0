package com.speakupcambridge.model.mapper;

import com.speakupcambridge.model.MailchimpLocalPerson;
import com.speakupcambridge.model.MailchimpPerson;
import com.speakupcambridge.model.MailchimpRemotePerson;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OneToOneMapper {
  OneToOneMapper INSTANCE = Mappers.getMapper(OneToOneMapper.class);

  MailchimpPerson toMailchimpPerson(MailchimpPerson mailchimpPerson);

  MailchimpPerson toMailchimpPerson(MailchimpRemotePerson mailchimpRemotePerson);

  MailchimpPerson toMailchimpPerson(MailchimpLocalPerson mailchimpRemotePerson);

  MailchimpRemotePerson toMailchimpRemotePerson(MailchimpPerson mailchimpPerson);

  MailchimpRemotePerson toMailchimpRemotePerson(MailchimpRemotePerson mailchimpRemotePerson);

  MailchimpRemotePerson toMailchimpRemotePerson(MailchimpLocalPerson mailchimpRemotePerson);

  MailchimpLocalPerson toMailchimpLocalPerson(MailchimpPerson mailchimpPerson);

  MailchimpLocalPerson toMailchimpLocalPerson(MailchimpRemotePerson mailchimpRemotePerson);

  MailchimpLocalPerson toMailchimpLocalPerson(MailchimpLocalPerson mailchimpRemotePerson);
}

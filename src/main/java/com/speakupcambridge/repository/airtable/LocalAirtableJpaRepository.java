package com.speakupcambridge.repository.airtable;

import com.speakupcambridge.model.airtable.AirtableRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface LocalAirtableJpaRepository<T extends AirtableRecord>
    extends JpaRepository<T, String> {}

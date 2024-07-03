package com.speakupcambridge.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
public class AppConfig {
  public AirtableConfig airtableConfig;
}

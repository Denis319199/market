package com.db.app.configuration;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EntityScan(basePackages = "com.db.model")
@EnableJpaRepositories(basePackages = "com.db.repo")
@EnableTransactionManagement
public class DataBaseConfiguration {}

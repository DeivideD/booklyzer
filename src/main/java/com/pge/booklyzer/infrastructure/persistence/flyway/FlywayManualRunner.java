package com.pge.booklyzer.infrastructure.persistence.flyway;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(100)
public class FlywayManualRunner implements CommandLineRunner {

  private final Flyway flyway;

  @Autowired
  public FlywayManualRunner(Flyway flyway) {
    this.flyway = flyway;
  }

  @Override
  public void run(String... args) {
    System.out.println("▶ Executando migrações do Flyway...");
    try {
      flyway.baseline();
    } catch (Exception ignored) {}
    flyway.migrate();
  }
}
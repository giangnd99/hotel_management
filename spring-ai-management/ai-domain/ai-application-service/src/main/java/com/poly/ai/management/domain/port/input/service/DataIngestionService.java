package com.poly.ai.management.domain.port.input.service;

import org.springframework.boot.CommandLineRunner;

public interface DataIngestionService extends CommandLineRunner {

    void ingestHotelData();
}

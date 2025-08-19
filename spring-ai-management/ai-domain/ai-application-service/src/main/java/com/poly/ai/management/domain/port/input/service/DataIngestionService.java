package com.poly.ai.management.domain.port.input.service;

import org.springframework.boot.CommandLineRunner;

import java.io.InputStream;

public interface DataIngestionService extends CommandLineRunner {

    void ingestHotelData();
    void ingestFile(InputStream fileStream, String fileName);
}

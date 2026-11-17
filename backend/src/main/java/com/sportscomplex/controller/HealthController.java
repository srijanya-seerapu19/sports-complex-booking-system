package com.sportscomplex.controller;

import java.time.Instant;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/health")
public class HealthController {

    private final DataSource dataSource;

    public HealthController(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @GetMapping
    public Map<String, Object> health() {
        String dbInfo = "Unknown";
        try (var conn = dataSource.getConnection()) {
            dbInfo = conn.getMetaData().getDatabaseProductName() + " " + conn.getMetaData().getDatabaseProductVersion();
        } catch (Exception ex) {
            dbInfo = "Unavailable: " + ex.getMessage();
        }

        return Map.of(
            "status", "UP",
            "service", "sports-complex-booking-system",
            "database", dbInfo,
            "timestamp", Instant.now().toString()
        );
    }
}

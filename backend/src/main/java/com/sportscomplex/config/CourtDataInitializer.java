package com.sportscomplex.config;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.sportscomplex.model.Court;
import com.sportscomplex.repository.CourtRepository;

@Component
public class CourtDataInitializer implements CommandLineRunner {

    private final CourtRepository courtRepository;

    public CourtDataInitializer(CourtRepository courtRepository) {
        this.courtRepository = courtRepository;
    }

    @Override
    public void run(String... args) {
        List<Court> starterCourts = List.of(
            new Court("Badminton Court 1", "Badminton", "Sports Complex - Indoor Hall A"),
            new Court("Badminton Court 2", "Badminton", "Sports Complex - Indoor Hall A"),
            new Court("Badminton Court 3", "Badminton", "Sports Complex - Indoor Hall B"),
            new Court("Basketball Court 1", "Basketball", "Outdoor Court Complex"),
            new Court("Basketball Court 2", "Basketball", "Outdoor Court Complex"),
            new Court("Cricket Ground", "Cricket", "Main Campus Athletics Ground"),
            new Court("Cricket Practice Nets", "Cricket", "Athletics Ground East"),
            new Court("Football Ground", "Football", "Main Campus Athletics Ground"),
            new Court("Tennis Court 1", "Tennis", "Sports Complex West"),
            new Court("Tennis Court 2", "Tennis", "Sports Complex West"),
            new Court("Volleyball Court 1", "Volleyball", "Sports Complex Sand Court"),
            new Court("Volleyball Court 2", "Volleyball", "Sports Complex Hard Court"),
            new Court("Table Tennis Court 1", "Table Tennis", "Indoor Recreation Hall"),
            new Court("Table Tennis Court 2", "Table Tennis", "Indoor Recreation Hall"),
            new Court("Carrom Board 1", "Carrom", "Indoor Recreation Room"),
            new Court("Carrom Board 2", "Carrom", "Indoor Recreation Room"),
            new Court("Chess Board 1", "Chess", "Indoor Recreation Room"),
            new Court("Chess Board 2", "Chess", "Indoor Recreation Room")
        );

        try {
            List<String> existingNames = courtRepository.findAll()
                .stream()
                .map(Court::getName)
                .toList();

            List<Court> missingCourts = starterCourts.stream()
                .filter(court -> !existingNames.contains(court.getName()))
                .toList();

            if (!missingCourts.isEmpty()) {
                courtRepository.saveAll(missingCourts);
            }
        } catch (Exception ex) {
            // Quiet fallback during initialization
        }
    }
}

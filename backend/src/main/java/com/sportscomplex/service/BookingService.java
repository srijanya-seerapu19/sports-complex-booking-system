package com.sportscomplex.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.sportscomplex.model.Booking;
import com.sportscomplex.model.Court;
import com.sportscomplex.model.Student;
import com.sportscomplex.repository.BookingRepository;
import com.sportscomplex.repository.CourtRepository;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final CourtRepository courtRepository;

    public BookingService(
            BookingRepository bookingRepository,
            CourtRepository courtRepository) {
        this.bookingRepository = bookingRepository;
        this.courtRepository = courtRepository;
    }

    @Transactional
    public BookingResponse createBooking(Long courtId, Integer durationMinutes, Student student) {
        if (student == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Please sign in to reserve a court.");
        }

        if (durationMinutes == null || durationMinutes < 15 || durationMinutes > 60) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Duration must be between 15 and 60 minutes."
            );
        }

        Court court = courtRepository.findById(courtId)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Court not found."
            ));

        if (!court.isActive()) {
            throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "This court is currently closed for maintenance."
            );
        }

        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime endTime = startTime.plusMinutes(durationMinutes);

        // Fair-play concurrency check 1: Student active booking lock
        boolean studentHasActiveBooking = bookingRepository
            .existsByStudent_IdAndStatusAndEndTimeAfter(
                student.getId(),
                "ACTIVE",
                startTime
            );

        if (studentHasActiveBooking) {
            throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "You already have an active booking in progress. Please finish that session first."
            );
        }

        // Fair-play concurrency check 2: Court occupancy lock
        boolean courtOccupied = bookingRepository
            .existsByCourt_IdAndStatusAndEndTimeAfter(
                court.getId(),
                "ACTIVE",
                startTime
            );

        if (courtOccupied) {
            throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "This court is currently occupied. Please choose another court."
            );
        }

        String sportPrefix = court.getSport().replaceAll("[^A-Za-z]", "").toUpperCase(Locale.ROOT);
        if (sportPrefix.length() > 4) {
            sportPrefix = sportPrefix.substring(0, 4);
        }
        String randomSuffix = UUID.randomUUID().toString().replace("-", "").substring(0, 4).toUpperCase(Locale.ROOT);
        String bookingCode = "AU-" + sportPrefix + "-" + randomSuffix;

        Booking booking = new Booking(
            bookingCode,
            court,
            student,
            startTime,
            endTime,
            durationMinutes
        );

        return toResponse(bookingRepository.save(booking));
    }

    @Transactional(readOnly = true)
    public List<BookingResponse> getMyBookings(Student student) {
        if (student == null) {
            return List.of();
        }
        return bookingRepository
            .findAllByStudent_IdOrderByStartTimeDesc(student.getId())
            .stream()
            .map(this::toResponse)
            .toList();
    }

    @Transactional
    public BookingResponse cancelBooking(Long bookingId, Student student) {
        if (student == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Please sign in to cancel a booking.");
        }

        Booking booking = bookingRepository.findById(bookingId)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Booking not found."
            ));

        if (!booking.getStudent().getId().equals(student.getId())) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Booking not found."
            );
        }

        if (!"ACTIVE".equals(booking.getStatus()) ||
                !booking.getEndTime().isAfter(LocalDateTime.now())) {
            throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "This booking is no longer active."
            );
        }

        booking.setStatus("CANCELLED");
        return toResponse(bookingRepository.save(booking));
    }

    public BookingResponse toResponse(Booking booking) {
        return new BookingResponse(
            booking.getId(),
            booking.getBookingCode(),
            booking.getCourt().getId(),
            booking.getCourt().getName(),
            booking.getCourt().getSport(),
            booking.getStudent().getEmail(),
            booking.getStartTime(),
            booking.getEndTime(),
            booking.getDurationMinutes(),
            booking.getStatus()
        );
    }

    public record BookingResponse(
        Long id,
        String bookingCode,
        Long courtId,
        String courtName,
        String sport,
        String studentEmail,
        LocalDateTime startTime,
        LocalDateTime endTime,
        Integer durationMinutes,
        String status
    ) {
    }
}

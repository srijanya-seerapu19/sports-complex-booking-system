package com.sportscomplex.controller;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.sportscomplex.model.Student;
import com.sportscomplex.repository.StudentRepository;
import com.sportscomplex.service.BookingService;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;
    private final StudentRepository studentRepository;

    public BookingController(
            BookingService bookingService,
            StudentRepository studentRepository) {
        this.bookingService = bookingService;
        this.studentRepository = studentRepository;
    }

    private Student getAuthenticatedStudent(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()
                || "anonymousUser".equals(authentication.getPrincipal())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Please sign in to continue.");
        }
        return studentRepository.findByEmailIgnoreCase(authentication.getName())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found."));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookingService.BookingResponse createBooking(
            @Valid @RequestBody CreateBookingRequest request,
            Authentication authentication) {
        Student student = getAuthenticatedStudent(authentication);
        return bookingService.createBooking(
            request.courtId(),
            request.durationMinutes(),
            student
        );
    }

    @GetMapping("/mine")
    public List<BookingService.BookingResponse> getMyBookings(Authentication authentication) {
        Student student = getAuthenticatedStudent(authentication);
        return bookingService.getMyBookings(student);
    }

    @DeleteMapping("/{bookingId}")
    public BookingService.BookingResponse cancelBooking(
            @PathVariable Long bookingId,
            Authentication authentication) {
        Student student = getAuthenticatedStudent(authentication);
        return bookingService.cancelBooking(bookingId, student);
    }

    public record CreateBookingRequest(
        @NotNull Long courtId,
        @NotNull @Min(15) @Max(60) Integer durationMinutes
    ) {
    }
}

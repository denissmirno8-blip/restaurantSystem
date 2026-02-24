package com.example.demo.controller;

import com.example.demo.repository.Booking;
import com.example.demo.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Book;
import java.util.List;

@RestController
@RequestMapping(path = "/api/booking")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping
    public ResponseEntity<List<Booking>> getBooking(){
        return ResponseEntity.ok(bookingService.findAll());
    }

    @PostMapping
    public ResponseEntity<Booking> create(@Valid @RequestBody Booking booking){
        return ResponseEntity.status(HttpStatus.CREATED).body(bookingService.create(booking));
    }

    @DeleteMapping(path = "{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        bookingService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PatchMapping(path = "{id}")
    public ResponseEntity<Booking> update(@PathVariable Long id, @RequestBody Booking booking){
        return ResponseEntity.ok(bookingService.update(id, booking.getClient(), booking.getStart(), booking.getTable()));
    }
}

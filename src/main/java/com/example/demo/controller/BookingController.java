package com.example.demo.controller;

import com.example.demo.repository.Area;
import com.example.demo.repository.Booking;
import com.example.demo.repository.RestaurantTable;
import com.example.demo.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Book;
import java.util.List;

@Controller
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

    /*@GetMapping(path = "/map")
    public ResponseEntity<List<Long>> getTablesForBooking(@RequestParam String date, @RequestParam String time, @RequestParam Integer size, @RequestParam String area, @RequestParam(required = false) List<String> preferences) {

        return ResponseEntity.ok(bookingService.findTablesForBooking(date, time, size, area, preferences));
    }*/
    @GetMapping(path = "/map")
    public String getMap() {

        return "map";
    }
    @GetMapping(path = "/avaiable_tables")
    @ResponseBody
    public ResponseEntity<List<Booking>> getTablesForBooking(@RequestParam String date, @RequestParam String time, @RequestParam Integer size, @RequestParam Long area, @RequestParam(required = false) List<Long> preferences) {
        return ResponseEntity.ok(bookingService.findTablesForBooking(date, time, size, area,preferences));
    }



    //@GetMapping(path = "get free time for tables")
    @GetMapping(path = "times")
    public ResponseEntity<List<String>> getBookingTime(){
        return ResponseEntity.ok(bookingService.findTime());
    }

    @PostMapping
    public ResponseEntity<Booking> create(@Valid @RequestBody Booking booking){
        return ResponseEntity.status(HttpStatus.CREATED).body(bookingService.create(booking));
    }

    @PostMapping(path="all")
    public ResponseEntity<String> createMultiple(@Valid @RequestBody List<Booking> bookings) {
        for (Booking booking : bookings) {
            bookingService.create(booking);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body("All bookings are created.");
    }

    @PostMapping(path = "book")
    public ResponseEntity<Booking> update(@RequestBody Booking booking){
        return ResponseEntity.ok(bookingService.update(booking.getClient(), booking.getDate(), booking.getTime(), booking.getTable()));
    }
}

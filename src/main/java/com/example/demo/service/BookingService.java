package com.example.demo.service;

import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;

    public BookingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    public List<Booking> findAll() {
        return bookingRepository.findAll();
    }

    public Booking create(Booking booking) {
        return bookingRepository.save(booking);
    }

    public void delete(Long id) {
        Optional<Booking> optionalBooking = bookingRepository.findById(id);
        if(optionalBooking.isEmpty())
            throw new ResourceNotFoundException("Booking with id: " + id + " doesn't exist.");
        
        bookingRepository.deleteById(id);
    }

    @Transactional
    public Booking update(Long id, Client client, LocalDateTime start, RestaurantTable restaurantTable) {
        Optional<Booking> optionalBooking = bookingRepository.findById(id);
        if(optionalBooking.isEmpty())
            throw new ResourceNotFoundException("Booking with id: " + id + " doesn't exist.");

        Booking booking = optionalBooking.get();
        if(client != null && !booking.getClient().equals(client))
            booking.setClient(client);

        if(restaurantTable != null && !booking.getTable().equals(restaurantTable))
            booking.setTable(restaurantTable);

        if(start != null && !booking.getStart().equals(start))
            booking.setStart(start);

        return booking;
    }
}

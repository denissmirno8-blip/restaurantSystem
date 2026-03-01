package com.example.demo.service;

import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final ClientRepository clientRepository;

    public BookingService(BookingRepository bookingRepository, ClientRepository clientRepository) {
        this.bookingRepository = bookingRepository;
        this.clientRepository = clientRepository;
    }

    public List<Booking> findAll() {
        return bookingRepository.findAll();
    }

    public Booking create(Booking booking) {
        return bookingRepository.save(booking);
    }

    //Registration
    @Transactional
    public Booking update(Client client, String date, String time, RestaurantTable restaurantTable) {
        Optional<Booking> optionalBooking = bookingRepository.findByDateAndTimeAndRestaurantTable(date, time, restaurantTable);
        if(optionalBooking.isEmpty())
            throw new ResourceNotFoundException("Booking with " + date + "  " + time + " and table nr " + restaurantTable.getId() + " doesn't exist.");

        Client existingClient = clientRepository.findByPhoneNumber(client.getPhoneNumber()).orElse(client);
        Booking booking = optionalBooking.get();
        booking.setClient(existingClient);

        return booking;
    }

    public List<String> findTime() {
        return bookingRepository.findDistinctTimes();
    }

    public List<Booking> findTablesForBooking(String date, String time) {

        return bookingRepository.findFreeTablesForBookingByDateAndTime(date, time);
    }
}

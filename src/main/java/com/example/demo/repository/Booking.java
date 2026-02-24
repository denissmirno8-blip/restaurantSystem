package com.example.demo.repository;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Entity
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "table_id")
    @NotNull(message = "Table name is mandatory.")
    private RestaurantTable restaurantTable;
    @ManyToOne
    @JoinColumn(name = "client_id")
    @NotNull(message = "Client is mandatory.")
    private Client client;
    @NotNull(message = "Start time is mandatory;")
    private LocalDateTime start;
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Status is mandatory.")
    private BookingStatus status;

    public Booking(BookingStatus status, LocalDateTime start, Client client, RestaurantTable restaurantTable, Long id) {
        this.status = status;
        this.start = start;
        this.client = client;
        this.restaurantTable = restaurantTable;
        this.id = id;
    }
    public Booking(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public RestaurantTable getTable() {
        return restaurantTable;
    }

    public void setTable(RestaurantTable restaurantTable) {
        this.restaurantTable = restaurantTable;
    }


}

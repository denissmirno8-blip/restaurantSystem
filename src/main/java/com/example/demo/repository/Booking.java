package com.example.demo.repository;

import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.lang.String;

@Entity
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "table_id")
    @NotNull(message = "Table name is mandatory.")
    private RestaurantTable restaurantTable;
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "client_id")
    private Client client;
    @NotNull(message = "date is mandatory;")
    private String date;
    @NotBlank(message = "Time is mandatory.")
    private String time;
    private boolean tableIsFree;

    public Booking(String time, String date, Client client, RestaurantTable restaurantTable, Long id) {
        this.date = date;
        this.client = client;
        this.time = time;
        this.restaurantTable = restaurantTable;
        this.id = id;
        this.tableIsFree = (this.client == null);
    }
    public Booking(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;

        this.tableIsFree = (client == null);
    }

    public RestaurantTable getTable() {
        return restaurantTable;
    }

    public void setTable(RestaurantTable restaurantTable) {
        this.restaurantTable = restaurantTable;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isTableFree() {
        return tableIsFree;
    }
}

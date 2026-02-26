package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    Optional<Booking> findByDateAndTimeAndRestaurantTable(String date, String time, RestaurantTable restaurantTable);

    @Query("SELECT DISTINCT b.time FROM Booking b ORDER BY b.time ASC")
    List<String> findDistinctTimes();

    @Query("SELECT b.restaurantTable.id FROM Booking b WHERE b.date = :date AND b.time = :time AND b.restaurantTable.area.name = :area AND b.restaurantTable.size >= :size AND b.client IS NULL")
    List<Long> findFreeTablesForBooking(@Param("date") String date, @Param("time")String time, @Param("size") Integer size, @Param("area") String area);

    @Query("SELECT b.restaurantTable.id FROM Booking b JOIN b.restaurantTable.preferences p WHERE b.date = :date AND b.time = :time AND b.restaurantTable.area.name = :area AND b.restaurantTable.size >= :size AND b.client IS NULL AND p.preference IN :preferences")
    List<Long> findFreeTablesForBookingWithPreferences(@Param("date") String date, @Param("time")String time, @Param("size") Integer size, @Param("area") String area, @Param("preferences") List<String> preferences);
}

package com.example.demo.service;

import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TableService {

    private final TableRepository tableRepository;
    private final PreferenceRepository preferenceRepository;

    public TableService(TableRepository tableRepository, PreferenceRepository preferenceRepository) {
        this.tableRepository = tableRepository;
        this.preferenceRepository = preferenceRepository;
    }

    public List<RestaurantTable> findAll() {
        return tableRepository.findAll();
    }

    public RestaurantTable create(RestaurantTable restaurantTable) {
        return tableRepository.save(restaurantTable);
    }

    public void delete(Long id) {
        Optional<RestaurantTable> optionalTable = tableRepository.findById(id);
        if(optionalTable.isEmpty())
            throw new ResourceNotFoundException("Table with id: " + id + "doesn't exist.");

        tableRepository.deleteById(id);
    }

    @Transactional
    public RestaurantTable update(Long id, Area area, Integer size, List<Preference> preferences) {
        Optional<RestaurantTable> optionalTable = tableRepository.findById(id);
        if(optionalTable.isEmpty())
            throw new ResourceNotFoundException("Table with id: " + id + "doesn't exist.");

        RestaurantTable restaurantTable = optionalTable.get();

        if(size != null && !restaurantTable.getSize().equals(size))
            restaurantTable.setSize(size);
        if(area != null && !area.equals(restaurantTable.getArea()))
            restaurantTable.setArea(area);

        if(preferences != null){

            List<Preference> managedPreferences = preferences.stream()
                                                             .map(p -> preferenceRepository.findById(p.getId()).orElseThrow(() -> new ResourceNotFoundException("Preference with id: " + p.getId() + " doesn't exist.")))
                                                             .toList();

            restaurantTable.setPreferences(managedPreferences);
        }
        return restaurantTable;
    }
}

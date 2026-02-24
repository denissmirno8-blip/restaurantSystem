package com.example.demo.controller;

import com.example.demo.repository.RestaurantTable;
import com.example.demo.service.TableService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tables")
public class TableController {

    private final TableService tableService;

    public TableController(TableService tableService) {
        this.tableService = tableService;
    }

    @GetMapping
    public ResponseEntity<List<RestaurantTable>> getTables(){
        return ResponseEntity.ok(tableService.findAll());
    }

    @PostMapping
    public ResponseEntity<RestaurantTable> create(@Valid @RequestBody RestaurantTable restaurantTable){
        return ResponseEntity.status(HttpStatus.CREATED).body(tableService.create(restaurantTable));
    }

    @PostMapping(path="all")
    public ResponseEntity<String> createMultiple(@Valid @RequestBody List<RestaurantTable> restaurantTables) {
        for (RestaurantTable restaurantTable : restaurantTables) {
            tableService.create(restaurantTable);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body("Alla tables are created.");
    }

    @DeleteMapping(path = "{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        tableService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PatchMapping(path = "{id}")
    public ResponseEntity<RestaurantTable> update(@PathVariable Long id, @RequestBody RestaurantTable restaurantTable){
        return ResponseEntity.ok(tableService.update(id, restaurantTable.getArea(), restaurantTable.getSize(), restaurantTable.getPreferences()));
    }
}

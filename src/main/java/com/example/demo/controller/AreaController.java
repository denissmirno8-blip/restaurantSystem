package com.example.demo.controller;

import com.example.demo.repository.Area;
import com.example.demo.service.AreaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/areas")
public class AreaController {

    private final AreaService areaService;

    public AreaController(AreaService areaService) {
        this.areaService = areaService;
    }

    @GetMapping
    public ResponseEntity<List<Area>> findAll(){
        return ResponseEntity.ok(areaService.findAll());
    }

    @PostMapping
    public ResponseEntity<Area> create(@Valid @RequestBody Area area){
        return ResponseEntity.status(HttpStatus.CREATED).body(areaService.create(area));
    }

    @PostMapping(path="all")
    public ResponseEntity<String> createMultiple(@Valid @RequestBody List<Area> areas) {
        for (Area area : areas) {
            areaService.create(area);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body("All areas are created.");
    }

    @DeleteMapping(path = "{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        areaService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    @PatchMapping(path = "{id}")
    public ResponseEntity<Area> update(@PathVariable Long id, @RequestBody Area area){
        return ResponseEntity.ok(areaService.update(id, area.getName()));
    }
}

package com.example.demo.controller;

import com.example.demo.repository.Preference;
import com.example.demo.service.PreferenceService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.awt.event.PaintEvent;
import java.util.List;

@RestController
@RequestMapping(path = "/api/preferences")
public class PreferenceController {

    private final PreferenceService preferenceService;

    public PreferenceController(PreferenceService preferenceService) {
        this.preferenceService = preferenceService;
    }

    @GetMapping
    public ResponseEntity<List<Preference>> getPreferences(){
        return ResponseEntity.ok(preferenceService.findAll());
    }

    @PostMapping
    public ResponseEntity<Preference> create(@Valid @RequestBody Preference preference){
        return ResponseEntity.status(HttpStatus.CREATED).body(preferenceService.create(preference));
    }

    @PostMapping(path="all")
    public ResponseEntity<String> createMultiple(@Valid @RequestBody List<Preference> preferences) {
        for (Preference preference : preferences) {
            preferenceService.create(preference);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body("All preference are created.");
    }

    @DeleteMapping(path = "{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        preferenceService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PatchMapping(path = "{id}")
    public ResponseEntity<Preference> update(@PathVariable Long id, @RequestBody Preference preference){
        return ResponseEntity.ok(preferenceService.update(id, preference.getPreference()));
    }
}

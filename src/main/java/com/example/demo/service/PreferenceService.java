package com.example.demo.service;

import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.repository.Preference;
import com.example.demo.repository.PreferenceRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PreferenceService {

    private final PreferenceRepository preferenceRepository;

    public PreferenceService(PreferenceRepository preferenceRepository) {
        this.preferenceRepository = preferenceRepository;
    }

    public List<Preference> findAll() {
        return preferenceRepository.findAll();
    }

    public Preference create(Preference preference) {
        return preferenceRepository.save(preference);
    }

    public void delete(Long id) {
        Optional<Preference> optionalPreference = preferenceRepository.findById(id);
        if(optionalPreference.isEmpty())
            throw new ResourceNotFoundException("Preference with id: " + id + "doesn't exist.");

        preferenceRepository.deleteById(id);
    }

    @Transactional
    public Preference update(Long id, String preferenceName) {
        Optional<Preference> optionalPreference = preferenceRepository.findById(id);
        if(optionalPreference.isEmpty())
            throw new ResourceNotFoundException("Preference with id: " + id + "doesn't exist.");

        Preference preference = optionalPreference.get();
        if(preferenceName != null && !preference.getPreference().equals(preferenceName))
            preference.setPreference(preferenceName);
        return preference;
    }
}

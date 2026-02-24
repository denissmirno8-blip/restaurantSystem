package com.example.demo.service;

import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.repository.Area;
import com.example.demo.repository.AreaRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AreaService {

    private final AreaRepository areaRepository;

    public AreaService(AreaRepository areaRepository) {
        this.areaRepository = areaRepository;
    }

    public List<Area> findAll() {
        return areaRepository.findAll();
    }

    public Area create(Area area) {
        return areaRepository.save(area);
    }

    public void delete(Long id) {
        Optional<Area> optionalArea = areaRepository.findById(id);
        if(optionalArea.isEmpty())
            throw new ResourceNotFoundException("Area with id:" + id + "doesn't exist.");

        areaRepository.deleteById(id);
    }

    @Transactional
    public Area update(Long id, String name) {
        Optional<Area> optionalArea = areaRepository.findById(id);
        if(optionalArea.isEmpty())
            throw new ResourceNotFoundException("Area with id:" + id + "doesn't exist.");
        Area area = optionalArea.get();
        if(name != null && !area.getName().equals(name))
            area.setName(name);
        return area;
    }
}

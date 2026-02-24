package com.example.demo.repository;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;

@Entity
public class Area {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "Area name is mandatory.")
    private String name;

    public Area(Long id, String name) {
        this.id = id;
        this.name = name;
    }
    public Area(){
    }

    public Long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o){
        if(this == o) return true;

        if(o == null || getClass() != o.getClass()) return false;

        Area area = (Area) o;

        return id != null && id.equals(area.getId());
    }

    @Override
    public int hashCode(){
        return getClass().hashCode();
    }
}

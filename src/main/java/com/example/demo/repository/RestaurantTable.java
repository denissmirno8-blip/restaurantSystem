package com.example.demo.repository;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;

@Entity
@jakarta.persistence.Table(name = "restaurant_tables")
public class RestaurantTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Positive
    private Integer size;
    @ManyToOne
    @JoinColumn(name = "area_id")
    @NotNull(message = "Area is mandatory.")
    private Area area;

    @ManyToMany
    @JoinTable(
            name="table_preferences",
            joinColumns = @JoinColumn(name="table_id"),
            inverseJoinColumns = @JoinColumn(name="preference_id")
    )
    private List<Preference> preferences;

    public RestaurantTable(Long id, Integer size, List<Preference> preferences, Area area) {
        this.id = id;
        this.size = size;
        this.preferences = preferences;
        this.area = area;
    }

    public RestaurantTable(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    public List<Preference> getPreferences() {
        return preferences;
    }

    public void setPreferences(List<Preference> preferences) {
        this.preferences = preferences;
    }

    @Override
    public boolean equals(Object o){
        if(this == o) return true;

        if(o == null || getClass() != o.getClass()) return false;

        RestaurantTable restaurantTable = (RestaurantTable) o;

        return id != null && id.equals(restaurantTable.getId());
    }

    @Override
    public int hashCode(){
        return getClass().hashCode();
    }
}

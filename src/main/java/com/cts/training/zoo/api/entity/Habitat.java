package com.cts.training.zoo.api.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Habitat {

    @Id
    private int id;

    private String type;
    private String name;

    private int occupiedCount= 0;
    private int maxOccupationAllowed = 0;

    public Habitat(){}

    public Habitat(int id, String type, String name, int maxOccupationAllowed) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.maxOccupationAllowed = maxOccupationAllowed;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOccupiedCount() {
        return occupiedCount;
    }

    public void setOccupiedCount(int occupiedCount) {
        this.occupiedCount = occupiedCount;
    }

    public int getMaxOccupationAllowed() {
        return maxOccupationAllowed;
    }
}

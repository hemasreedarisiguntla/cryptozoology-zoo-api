package com.cts.training.zoo.api.cryptozoologyzooapi.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Habitat {

    @GeneratedValue
    @Id
    private Integer id;

    private String type;
    private String name;

    public Habitat(String type, String name) {
        this.name = name;
        this.type = type;
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
}

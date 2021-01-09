package com.cts.training.zoo.api.cryptozoologyzooapi.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
public class Animal {
    @Id
    @GeneratedValue
    private Integer id;

    private String name;

    private String type;

    @ManyToOne(cascade = CascadeType.ALL)
    private Habitat habitat;

    private String mood = "unhappy";

    public Animal() {

    }

    public Animal(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public Habitat getHabitat() {
        return habitat;
    }

    public void setHabitat(Habitat habitat) {
        this.habitat = habitat;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMood() {
        return mood;
    }

    public void setMood(String mood) {
        this.mood = mood;
    }
}

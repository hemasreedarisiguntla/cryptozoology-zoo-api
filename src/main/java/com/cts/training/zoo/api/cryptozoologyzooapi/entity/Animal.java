package com.cts.training.zoo.api.cryptozoologyzooapi.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
public class Animal {
    @Id
    @GeneratedValue
    private Integer id;

    private String name;

    private String type;

    public Animal(String name, String type) {
        this.name = name;
        this.type = type;
    }
}

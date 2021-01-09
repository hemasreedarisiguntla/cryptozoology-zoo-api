package com.cts.training.zoo.api.cryptozoologyzooapi.service;

import com.cts.training.zoo.api.cryptozoologyzooapi.entity.Animal;
import com.cts.training.zoo.api.cryptozoologyzooapi.entity.Habitat;
import com.cts.training.zoo.api.cryptozoologyzooapi.repository.AnimalRepository;
import com.cts.training.zoo.api.cryptozoologyzooapi.repository.HabitatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class AnimalService {

    @Autowired
    AnimalRepository animalRepository;

    @Autowired
    HabitatRepository habitatRepository;

    public Animal addAnimal(Animal animal) {
        return animalRepository.save(animal);
    }

    public List<Animal> getAllAnimals() {
        return animalRepository.findAll();
    }

    public Animal updateAnimalMood(Integer id) {
        Animal animal;
        try {
            animal = animalRepository.findById(id).get();
            if (animal.getMood().equals("unhappy")) {
                animal.setMood("happy");
                animalRepository.save(animal);
            }

        } catch (NoSuchElementException e) {
            animal = null;
        }
        return animal;
    }

    public Animal updateAnimalHabitat(Integer animalId, String habitatName) {
        Animal animal;
        try {
            animal = animalRepository.findById(animalId).get();

            if(animal.getHabitat()==null) {
                Habitat newHabitat = habitatRepository.findByName(habitatName);
                animal.setHabitat(newHabitat);
            }
        } catch (NoSuchElementException e) {
            animal = null;
        }
        return animal;
    }
}

package com.cts.training.zoo.api.cryptozoologyzooapi.controller;

import com.cts.training.zoo.api.cryptozoologyzooapi.entity.Animal;
import com.cts.training.zoo.api.cryptozoologyzooapi.service.AnimalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/zoo/animals")
public class AnimalController {

    @Autowired
    AnimalService animalService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Animal addAnimal(@RequestBody Animal animal) {
        Animal newAnimal = animalService.addAnimal(animal);
        return newAnimal;
    }

    @GetMapping
    public List<Animal> getAllAnimals() {
        return animalService.getAllAnimals();
    }

    @PutMapping("/{id}")
    public Animal updateAnimalMood(@PathVariable Integer id) {
        return animalService.updateAnimalMood(id);
    }

    @PutMapping("/{animalId}/habitats/{habitatName}")
    public Animal updateAnimalHabitat(@PathVariable Integer animalId, @PathVariable String habitatName) {
        return animalService.updateAnimalHabitat(animalId, habitatName);
    }
}

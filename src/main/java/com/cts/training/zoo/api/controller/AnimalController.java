package com.cts.training.zoo.api.controller;

import com.cts.training.zoo.api.entity.Animal;
import com.cts.training.zoo.api.service.AnimalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 *Animal API handler methods.
 */
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

    /**
     * Finds all the animals which are matched with mood and type provided.
     *
     * @param mood
     * @param type
     * @return animals matched
     */
    @GetMapping("/mood/{mood}/type/{type}")
    public List<Animal> findAllAnimals(@PathVariable String mood, @PathVariable String type) {
        return animalService.findAllAnimals(mood, type);
    }
}

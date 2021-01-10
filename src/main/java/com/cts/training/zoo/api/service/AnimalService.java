package com.cts.training.zoo.api.service;

import com.cts.training.zoo.api.entity.Animal;
import com.cts.training.zoo.api.entity.Habitat;
import com.cts.training.zoo.api.repository.AnimalRepository;
import com.cts.training.zoo.api.repository.HabitatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class AnimalService {

    @Autowired
    AnimalRepository animalRepository;

    @Autowired
    HabitatRepository habitatRepository;

    /**
     * Add new {@link Animal}
     *
     * @param animal
     * @return added animal along with id.
     */
    public Animal addAnimal(Animal animal) {

        return animalRepository.save(animal);
    }

    /**
     * Final all the animals in the zoo.
     *
     * @return animal list
     */
    public List<Animal> getAllAnimals() {

        return animalRepository.findAll();
    }

    /**
     * Updates Animal mood.
     *
     * @param id
     * @return updated Animal instance of {@link Animal}
     */
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

    /**
     * Updates the Animal habitat.
     *
     * @param animalId
     * @param habitatName
     * @return updatedAnimal instance of {@link Animal}
     */
    public Animal updateAnimalHabitat(Integer animalId, String habitatName) {
        Animal animal;
        try {
            animal = animalRepository.findById(animalId).get();

            Habitat habitat = habitatRepository.findByName(habitatName);
            if (habitat != null) {
                if (!habitat.getType().equalsIgnoreCase(animal.getType())) {
                    animal.setMood("unhappy");
                } else {
                    if( habitat.getOccupiedCount() < habitat.getMaxOccupationAllowed()) {
                        habitat.setOccupiedCount(habitat.getOccupiedCount() + 1);
                        animal.setHabitat(habitat);
                    }
                }
                animalRepository.save(animal);
            }
        } catch (NoSuchElementException e) {
            animal = null;
        }
        return animal;
    }
}

package com.cts.training.zoo.api.cryptozoologyzooapi;

import com.cts.training.zoo.api.cryptozoologyzooapi.entity.Animal;
import com.cts.training.zoo.api.cryptozoologyzooapi.service.AnimalService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.collection.IsCollectionWithSize;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class CryptozoologyZooApiApplicationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @Autowired
    AnimalService animalService;

    @Test
    void contextLoads() {
    }

    /**
     * As zookeeper, I want to add animals to my zoo.
     * Rule: Animal should have a name and a type (flying, swimming, walking)
     * <p>
     * When I add an animalDto
     * Then it is in my zoo
     *
     * @throws Exception
     */
    @Test
    void testAddAnimal() throws Exception {
        Animal animal = new Animal("Lion", "walking");
        mockMvc.perform(
                post("/api/zoo/animals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(animal))
        ).andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value(animal.getName()))
                .andExpect(jsonPath("$.type").value(animal.getType()));
    }

    /**
     * As zookeeper, I want to view animals of my zoo.
     * <p>
     * Given I have added animals to my zoo
     * When I check my zoo
     * Then I see all the animals
     */
    @Test
    public void testGetAllAnimals() throws Exception {

        Animal animal1 = animalService.addAnimal(new Animal("goldenfish", "swimming"));
        Animal animal2 = animalService.addAnimal(new Animal("crow", "flying"));
        Animal animal3 = animalService.addAnimal(new Animal("lion", "walking"));

        mockMvc.perform(
                get("/api/zoo/animals")
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].id").value(animal1.getId()))
                .andExpect(jsonPath("$[0].name").value(animal1.getName()))
                .andExpect(jsonPath("$[0].type").value(animal1.getType()));

    }

    /**
     * As a zookeper, I want to feed my animals.
     * <p>
     * Rule: Animal moods are unhappy or happy. They are unhappy by default.
     * <p>
     * Given an animal is unhappy
     * When I give it a treat
     * Then the animal is happy
     */

    @Test
    void testUpdateAnimalMoodWithDefaultMood() throws Exception {
        Animal animal1 = animalService.addAnimal(new Animal("goldenfish", "swimming"));

        mockMvc.perform(
                put("/api/zoo/animals/{id}", animal1.getId())
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value(animal1.getName()))
                .andExpect(jsonPath("$.type").value(animal1.getType()))
                .andExpect(jsonPath("$.mood").value("happy"));
    }

    /**
     * As a zookeper, I want to feed my animals.
     * <p>
     * Rule: Animal moods are unhappy or happy. They are unhappy by default.
     * Given an animal is happy
     * When I give it a treat
     * Then the animal is still happy
     *
     * @throws Exception
     */
    @Test
    void testUpdateAnimalMoodWithHappyMood() throws Exception {
        Animal animal = new Animal("goldenfish", "swimming");
        animal.setMood("happy");
        Animal animal1 = animalService.addAnimal(animal);

        mockMvc.perform(
                put("/api/zoo/animals/{id}", animal1.getId())
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value(animal1.getName()))
                .andExpect(jsonPath("$.type").value(animal1.getType()))
                .andExpect(jsonPath("$.mood").value("happy"));
    }

}

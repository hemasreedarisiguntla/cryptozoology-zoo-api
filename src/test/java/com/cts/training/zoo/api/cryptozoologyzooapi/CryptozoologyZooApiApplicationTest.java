package com.cts.training.zoo.api.cryptozoologyzooapi;

import com.cts.training.zoo.api.cryptozoologyzooapi.entity.Animal;
import com.cts.training.zoo.api.cryptozoologyzooapi.entity.Habitat;
import com.cts.training.zoo.api.cryptozoologyzooapi.service.AnimalService;
import com.cts.training.zoo.api.cryptozoologyzooapi.service.HabitatService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.collection.IsCollectionWithSize;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
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
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class CryptozoologyZooApiApplicationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @Autowired
    AnimalService animalService;

    @Autowired
    HabitatService habitatService;

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

    /**
     * As a zookeeper, I want to maintain different types of habitats so that I can put different types of animals in them.
     * <p>
     * Given I have an empty <habitat>
     * When I put animal of <type> into a compatible habitat
     * Then the animal is in the habitat
     * <p>
     * Given I have an empty <habitat>
     * When I put animal of <type> into an incompatible habitat
     * Then the animal habitat should not change
     * And the animal becomes unhappy
     * <p>
     * Given I have an occuppied habitat
     * When I put an animal into the occupied habitat
     * Then the animal habitat should not change
     * <p>
     * |   type  |  habitat  |
     * | --------- | --------- |
     * | flying     |   nest    |
     * | swimming  |   ocean   |
     * | walking   |   forest  |
     */

    @Test
    public void testUpdateHabitatWithEmptyHabitat() throws Exception {

        Animal animal = new Animal("goldenfish", "swimming");
        Animal animal1 = animalService.addAnimal(animal);

        Habitat habitat = new Habitat("swimming", "ocean");
        Habitat habitat1 = habitatService.addHabitat(habitat);

        mockMvc.perform(
                put("/api/zoo/animals/{animalId}/habitats/{habitatName}", animal1.getId(), habitat.getName())
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value(animal1.getName()))
                .andExpect(jsonPath("$.type").value(animal1.getType()))
                .andExpect(jsonPath("$.habitat.name").value(animal1.getHabitat().getName()));
        //.andExpect(jsonPath("$.mood").value("happy"));

    }


}

package com.cts.training.zoo.api;

import com.cts.training.zoo.api.entity.Animal;
import com.cts.training.zoo.api.entity.Habitat;
import com.cts.training.zoo.api.service.AnimalService;
import com.cts.training.zoo.api.service.HabitatService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;
import java.util.LinkedList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration test cases to test the ZOO API handler methods with H2 DB.
 * <p>
 * |   type  |  habitat  |
 * | --------- | --------- |
 * | flying     |   nest    |
 * | swimming  |   ocean   |
 * | walking   |   forest  |
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class AnimalControllerIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @Autowired
    AnimalService animalService;

    @Autowired
    HabitatService habitatService;
    private List<Habitat> habitatList;

    /**
     * Setup the data which are required by test cases.
     */
    @BeforeEach
    public void setUp() {
        habitatList = new LinkedList<>();
        habitatList.add(habitatService.addHabitat(new Habitat(1, "swimming", "ocean", 300)));
        habitatList.add(habitatService.addHabitat(new Habitat(2, "flying", "nest", 500)));
        habitatList.add(habitatService.addHabitat(new Habitat(3, "walking", "forest", 2)));
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
    @DisplayName("addAnimal")
    @Test
    void testAddAnimal() throws Exception {
        Animal animal = new Animal("Lion", "walking");
        mockMvc.perform(
                post("/api/zoo/animals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(animal))
        ).andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
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
    @DisplayName("getAllAnimals")
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
                .andExpect(jsonPath("$[0].type").value(animal1.getType()))

                .andExpect(jsonPath("$[1].id").value(animal2.getId()))
                .andExpect(jsonPath("$[1].name").value(animal2.getName()))
                .andExpect(jsonPath("$[1].type").value(animal2.getType()))

                .andExpect(jsonPath("$[2].id").value(animal3.getId()))
                .andExpect(jsonPath("$[2].name").value(animal3.getName()))
                .andExpect(jsonPath("$[2].type").value(animal3.getType()));

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
    @DisplayName("updateAnimalMood - WithDefaultMood")
    @Test
    void testUpdateAnimalMoodWithDefaultMood() throws Exception {
        Animal animal1 = animalService.addAnimal(new Animal("goldenfish", "swimming"));

        mockMvc.perform(
                put("/api/zoo/animals/{id}", animal1.getId())
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
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
    @DisplayName("updateAnimalMood - WithHappyMood")
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
     * |   type  |  habitat  |
     * | --------- | --------- |
     * | flying     |   nest    |
     * | swimming  |   ocean   |
     * | walking   |   forest  |
     */
    @DisplayName("updateAnimalHabitat - WithEmptyHabitat")
    @Test
    public void testUpdateAnimalHabitatWithEmptyHabitat() throws Exception {

        Animal goldenFish = animalService.addAnimal(new Animal("goldenfish", "swimming"));
        Habitat oceanHabitat = habitatList.get(0);

        mockMvc.perform(
                put("/api/zoo/animals/{animalId}/habitats/{habitatName}", goldenFish.getId(), oceanHabitat.getName())
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value(goldenFish.getName()))
                .andExpect(jsonPath("$.type").value(goldenFish.getType()))
                .andExpect(jsonPath("$.habitat.name").value(goldenFish.getHabitat().getName()))
                .andExpect(jsonPath("$.habitat.occupiedCount").value(1));
    }

    /**
     * As a zookeeper, I want to maintain different types of habitats so that I can put different types of animals in them.
     * <p>
     * Given I have an empty <habitat>
     * When I put animal of <type> into an incompatible habitat
     * Then the animal habitat should not change
     * And the animal becomes unhappy
     *
     * @throws Exception
     */
    @DisplayName("updateAnimalHabitat - put animal into incompatible Habitat")
    @Test
    public void testUpdateAnimalHabitatWithIncompatibleHabitat() throws Exception {

        Animal goldenFish = new Animal("goldenfish", "swimming");
        goldenFish.setMood("happy");
        goldenFish.setHabitat(habitatList.get(0));
        Animal addedGoldenFish = animalService.addAnimal(goldenFish);

        mockMvc.perform(
                put("/api/zoo/animals/{animalId}/habitats/{habitatName}", addedGoldenFish.getId(), "nest")
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.id").value(addedGoldenFish.getId()))
                .andExpect(jsonPath("$.name").value(addedGoldenFish.getName()))
                .andExpect(jsonPath("$.type").value(addedGoldenFish.getType()))
                .andExpect(jsonPath("$.mood").value("unhappy"))
                .andExpect(jsonPath("$.habitat.name").value(addedGoldenFish.getHabitat().getName()));

    }

    /**
     * As a zookeeper, I want to maintain different types of habitats so that I can put different types of animals in them.
     * <p>
     * Given I have an occupied habitat
     * When I put an animal into the occupied habitat
     * Then the animal habitat should not change
     * <p>
     *
     * @throws Exception
     */
    @DisplayName("updateAnimalHabitat - put animal into occupied Habitat")
    @Test
    public void testUpdateAnimalHabitatWithOccupiedHabitat() throws Exception {
        Habitat forestHabitat = habitatList.get(2);
        Animal lion = new Animal("lion", "walking");
        forestHabitat.setOccupiedCount(1);
        lion.setHabitat(forestHabitat);
        animalService.addAnimal(lion);

        Animal tiger = new Animal("tiger", "walking");
        forestHabitat.setOccupiedCount(2);
        tiger.setHabitat(forestHabitat);
        animalService.addAnimal(tiger);

        Animal bear = new Animal("bear", "walking");
        Animal bearWithoutHabitat = animalService.addAnimal(bear);

        mockMvc.perform(
                put("/api/zoo/animals/{animalId}/habitats/{habitatName}", bearWithoutHabitat.getId(), "forest")
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.id").value(bearWithoutHabitat.getId()))
                .andExpect(jsonPath("$.name").value(bearWithoutHabitat.getName()))
                .andExpect(jsonPath("$.type").value(bearWithoutHabitat.getType()))
                .andExpect(jsonPath("$.habitat").doesNotExist());
    }

    /**
     * As a zookeeper, I want to search zoo data so that I can make reports on my zoo.
     * <p>
     * Given I have animals in my zoo
     * When I search for <mood> and <type>
     * Then I see a list of animals matching only <mood> and <type>
     *
     * @throws Exception
     */
    @DisplayName("findAnimals - nonEmpty list")
    @Test
    public void testFindAnimalsReturnValidList() throws Exception {
        addFewAnimalsForSearch();
        mockMvc.perform(
                get("/api/zoo/animals/mood/{mood}/type/{type}", "happy", "walking")
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }


    /**
     * As a zookeeper, I want to search zoo data so that I can make reports on my zoo.
     * <p>
     * Given I have habitats in my zoo
     * When I search for empty habitats
     * Then I see a list of empty habitats
     * <p>
     *
     * @throws Exception
     */
    @DisplayName("findAnimals - empty list")
    @Test
    public void testFindAnimalsReturnEmptyList() throws Exception {
        addFewAnimalsForSearch();
        mockMvc.perform(
                get("/api/zoo/animals/mood/{mood}/type/{type}", "unhappy", "flying")
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    /**
     * Add few animals as a data required for search.
     */
    private void addFewAnimalsForSearch() {
        Habitat forestHabitat = habitatList.get(2);
        Habitat oceanHabitat = habitatList.get(0);
        Habitat nestHabitat = habitatList.get(0);

        Animal lion = new Animal("lion", "walking");
        forestHabitat.setOccupiedCount(1);
        lion.setMood("happy");
        lion.setHabitat(forestHabitat);
        animalService.addAnimal(lion);

        Animal tiger = new Animal("tiger", "walking");
        forestHabitat.setOccupiedCount(2);
        tiger.setHabitat(forestHabitat);
        tiger.setMood("happy");
        animalService.addAnimal(tiger);

        Animal bear = new Animal("tiger", "walking");
        forestHabitat.setOccupiedCount(3);
        bear.setHabitat(forestHabitat);
        bear.setMood("unhappy");
        animalService.addAnimal(bear);

        Animal goldenFish = new Animal("goldenFish", "swimming");
        oceanHabitat.setOccupiedCount(1);
        goldenFish.setHabitat(oceanHabitat);
        goldenFish.setMood("happy");
        animalService.addAnimal(goldenFish);

        Animal peacock = new Animal("peacock", "flying");
        nestHabitat.setOccupiedCount(1);
        peacock.setHabitat(nestHabitat);
        peacock.setMood("happy");
        animalService.addAnimal(peacock);
    }
}

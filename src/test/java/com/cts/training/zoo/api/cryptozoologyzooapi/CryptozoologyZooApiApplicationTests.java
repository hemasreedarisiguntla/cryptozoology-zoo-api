package com.cts.training.zoo.api.cryptozoologyzooapi;

import com.cts.training.zoo.api.cryptozoologyzooapi.entity.Animal;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class CryptozoologyZooApiApplicationTests {

	@Autowired
	MockMvc mockMvc;

	@Autowired
	ObjectMapper mapper;

	@Test
	void contextLoads() {
	}

	@Test
	void addAnimals() throws Exception {
		Animal animal = new Animal("Lion","walking");
		mockMvc.perform(
				post("/api/zoo/animals")
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(animal))
		).andExpect(status().isCreated())
		.andExpect(jsonPath("$.id").exists());

	}

}

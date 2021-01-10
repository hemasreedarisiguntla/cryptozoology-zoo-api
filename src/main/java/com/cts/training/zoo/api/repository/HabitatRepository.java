package com.cts.training.zoo.api.repository;

import com.cts.training.zoo.api.entity.Habitat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HabitatRepository extends JpaRepository<Habitat, Integer> {
    Habitat findByName(String habitatName);
}

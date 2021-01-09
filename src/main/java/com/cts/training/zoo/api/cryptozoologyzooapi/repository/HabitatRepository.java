package com.cts.training.zoo.api.cryptozoologyzooapi.repository;

import com.cts.training.zoo.api.cryptozoologyzooapi.entity.Habitat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HabitatRepository extends JpaRepository<Habitat, Integer> {
    Habitat findByName(String habitatName);
}

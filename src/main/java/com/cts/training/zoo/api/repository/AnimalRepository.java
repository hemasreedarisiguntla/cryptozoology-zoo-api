package com.cts.training.zoo.api.repository;

import com.cts.training.zoo.api.entity.Animal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * JPA Repository for {@link Animal} {@link javax.persistence.Entity}
 */
@Repository
public interface AnimalRepository extends JpaRepository<Animal, Integer> {
    /**
     * Finds the animals which are matched with given mood and type.
     * @param mood
     * @param type
     * @return matched animals.
     */
    List<Animal> findByMoodAndType(String mood, String type);
}

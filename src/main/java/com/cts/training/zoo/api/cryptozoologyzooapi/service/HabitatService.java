package com.cts.training.zoo.api.cryptozoologyzooapi.service;

import com.cts.training.zoo.api.cryptozoologyzooapi.entity.Habitat;
import com.cts.training.zoo.api.cryptozoologyzooapi.repository.HabitatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HabitatService {

    @Autowired
    HabitatRepository habitatRepository;

    public Habitat addHabitat(Habitat habitat) {
        return habitatRepository.save(habitat);
    }
}

package com.takima.race.race.services;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.takima.race.race.entities.Race;
import com.takima.race.race.repositories.RaceRepository;

@Service
public class RaceService {

    private final RaceRepository raceRepository;

    public RaceService(RaceRepository raceRepository) {
        this.raceRepository = raceRepository;
    }

    public List<Race> getAll() {
        return raceRepository.findAll();
    }

    public Race getById(Long id) {
        return raceRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        String.format("Race %s not found", id)
                )
        );
    }

    public Race create(Race race) {
        return raceRepository.save(race);
    }

    public Race update(Long id, Race raceDetails) {
        Race existingRace = getById(id); 
        existingRace.setName(raceDetails.getName());
        existingRace.setDate(raceDetails.getDate());
        existingRace.setLocation(raceDetails.getLocation());
        existingRace.setMaxParticipants(raceDetails.getMaxParticipants());
        
        return raceRepository.save(existingRace);
    }
}
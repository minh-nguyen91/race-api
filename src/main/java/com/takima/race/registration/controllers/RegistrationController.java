package com.takima.race.registration.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.takima.race.registration.dto.RegistrationRequest;
import com.takima.race.registration.entities.Registration;
import com.takima.race.registration.services.RegistrationService;

@RestController
@RequestMapping("/races/{raceId}")
public class RegistrationController {

    private final RegistrationService registrationService;

    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @GetMapping("/participants/count")
    public Map<String, Integer> countParticipants(@PathVariable Long raceId) {
        return Map.of("count", registrationService.countParticipants(raceId));
    }

    @PostMapping("/registrations")
    @ResponseStatus(HttpStatus.CREATED)
    public Registration registerRunner(@PathVariable Long raceId, @RequestBody RegistrationRequest request) {
        return registrationService.register(raceId, request.runnerId());
    }

    @GetMapping("/registrations")
    public List<Registration> getParticipants(@PathVariable Long raceId) {
        return registrationService.getRegistrationsByRace(raceId);
    }
}
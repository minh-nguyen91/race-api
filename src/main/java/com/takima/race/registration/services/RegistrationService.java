package com.takima.race.registration.services;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.takima.race.race.entities.Race;
import com.takima.race.race.services.RaceService;
import com.takima.race.registration.entities.Registration;
import com.takima.race.registration.repositories.RegistrationRepository;
import com.takima.race.runner.entities.Runner;
import com.takima.race.runner.services.RunnerService;

@Service
public class RegistrationService {

    private final RegistrationRepository registrationRepository;
    private final RunnerService runnerService;
    private final RaceService raceService;

    public RegistrationService(RegistrationRepository registrationRepository, RunnerService runnerService, RaceService raceService) {
        this.registrationRepository = registrationRepository;
        this.runnerService = runnerService;
        this.raceService = raceService;
    }

    public Registration register(Long raceId, Long runnerId) {
        Race race = raceService.getById(raceId);
        Runner runner = runnerService.getById(runnerId);

        if (registrationRepository.existsByRunnerIdAndRaceId(runnerId, raceId)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Le coureur est déjà inscrit à cette course.");
        }

        if (registrationRepository.countByRaceId(raceId) >= race.getMaxParticipants()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "La course est complète.");
        }

        Registration registration = new Registration();
        registration.setRaceId(race.getId());
        registration.setRunnerId(runner.getId());
        registration.setRegistrationDate(LocalDate.now());

        return registrationRepository.save(registration);
    }

    public int countParticipants(Long raceId) {
        raceService.getById(raceId);
        return registrationRepository.countByRaceId(raceId);
    }

    public List<Registration> getRegistrationsByRace(Long raceId) {
        raceService.getById(raceId);
        return registrationRepository.findByRaceId(raceId);
    }

    public List<Race> getRacesByRunner(Long runnerId) {
        runnerService.getById(runnerId);
        List<Registration> registrations = registrationRepository.findByRunnerId(runnerId);
        
        return registrations.stream()
                .map(reg -> raceService.getById(reg.getRaceId()))
                .toList();
    }
}
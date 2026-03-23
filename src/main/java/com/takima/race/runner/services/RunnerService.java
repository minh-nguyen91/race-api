package com.takima.race.runner.services;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.takima.race.registration.repositories.RegistrationRepository;
import com.takima.race.runner.entities.Runner;
import com.takima.race.runner.repositories.RunnerRepository;

@Service
public class RunnerService {

    private final RunnerRepository runnerRepository;
    private final RegistrationRepository registrationRepository;

    public RunnerService(RunnerRepository runnerRepository, RegistrationRepository registrationRepository) {
        this.runnerRepository = runnerRepository;
        this.registrationRepository = registrationRepository;
    }

    public List<Runner> getAll() {
        return runnerRepository.findAll();
    }

    public Runner getById(Long id) {
        return runnerRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        String.format("Runner %s not found", id)
                )
        );
    }

    public Runner create(Runner runner) {
        validateEmail(runner.getEmail());
        return runnerRepository.save(runner);
    }

    public Runner update(Long id, Runner runnerDetails) {
        Runner existingRunner = getById(id);
        validateEmail(runnerDetails.getEmail());
        
        existingRunner.setFirstName(runnerDetails.getFirstName());
        existingRunner.setLastName(runnerDetails.getLastName());
        existingRunner.setEmail(runnerDetails.getEmail());
        existingRunner.setAge(runnerDetails.getAge());
        
        return runnerRepository.save(existingRunner);
    }

    @Transactional 
    public void delete(Long id) {
        Runner runner = getById(id);
        registrationRepository.deleteAll(registrationRepository.findByRunnerId(id));
        runnerRepository.delete(runner);
    }

    private void validateEmail(String email) {
        if (email == null || !email.contains("@")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid email address");
        }
    }
}
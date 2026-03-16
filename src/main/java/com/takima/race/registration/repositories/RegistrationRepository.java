package com.takima.race.registration.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.takima.race.registration.entities.Registration;

@Repository
public interface RegistrationRepository extends JpaRepository<Registration, Long> {
    List<Registration> findByRaceId(Long raceId);
    List<Registration> findByRunnerId(Long runnerId);
    boolean existsByRunnerIdAndRaceId(Long runnerId, Long raceId);
    int countByRaceId(Long raceId);
}
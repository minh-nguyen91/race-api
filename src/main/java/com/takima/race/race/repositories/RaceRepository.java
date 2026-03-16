package com.takima.race.race.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.takima.race.race.entities.Race;

@Repository
public interface RaceRepository extends JpaRepository<Race, Long> {}
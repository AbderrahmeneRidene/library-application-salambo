package com.sipacademy.stockmanager.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sipacademy.stockmanager.entities.Sitting;

public interface SittingRepository extends JpaRepository<Sitting, Integer> {

}

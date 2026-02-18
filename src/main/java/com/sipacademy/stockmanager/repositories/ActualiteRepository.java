package com.sipacademy.stockmanager.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sipacademy.stockmanager.entities.Actualite;

@Repository("actualiteRepository")
public interface ActualiteRepository extends JpaRepository<Actualite, Integer>{

}

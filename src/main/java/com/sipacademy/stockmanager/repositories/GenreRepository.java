package com.sipacademy.stockmanager.repositories;

import org.springframework.data.repository.CrudRepository;

import com.sipacademy.stockmanager.entities.Genre;

public interface GenreRepository extends CrudRepository<Genre, Integer>{

}

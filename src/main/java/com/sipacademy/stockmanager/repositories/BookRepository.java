package com.sipacademy.stockmanager.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.sipacademy.stockmanager.entities.Book;

@Repository
public interface BookRepository extends CrudRepository<Book, Integer> {
	
	

}

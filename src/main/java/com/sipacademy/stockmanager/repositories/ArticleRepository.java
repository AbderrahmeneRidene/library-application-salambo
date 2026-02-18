package com.sipacademy.stockmanager.repositories;

import org.springframework.data.repository.CrudRepository;

import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;

import com.sipacademy.stockmanager.entities.Article;


@Repository
public interface ArticleRepository extends CrudRepository<Article, Integer> {
	
	Page<Article> findAll(Pageable pageable);
	Iterable<Article> findByLabel(String label);

}

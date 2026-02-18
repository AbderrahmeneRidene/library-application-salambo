package com.sipacademy.stockmanager.repositories;

import java.util.List;
import org.springframework.data.domain.*;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.sipacademy.stockmanager.entities.Article;
import com.sipacademy.stockmanager.entities.Provider;


@Repository
public interface ProviderRepository extends CrudRepository<Provider, Integer> {
	
	@Query("FROM Article a WHERE a.provider.id = ?1")
	List<Article> findArticlesByProvider(int id);
	
	Page<Provider> findAll(Pageable pageable);


}

package com.sipacademy.stockmanager.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Book {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	private String title;
	private String author;
	
	// Le numéro ISBN (International Standard Book Number) unique du livre.
	private String isbn;
	private LocalDate publishedDate;
	
	// La langue dans laquelle le livre est écrit.
	private String language;
	
	private String coverViewImage;
	
	// Image pour la vue arrière (unique)
    private String backViewImage;
	
	 // Images pour le résumé (plusieurs)
    private List<String> summaryImages;
    
 // Images pour le sommaire (plusieurs)
    private List<String> tableOfContentsImages;
	
	// Le nombre d'exemplaires disponibles en stock.
	private Integer stockQuantity;
	
	// Localisation du livre dans une bibliothèque (e.g., Section A, Rayon 5).
	private String locationInLibrary;
	
	// La date et l'heure de création de l'entité.
	private LocalDateTime createdAt;
	
	// La date et l'heure de la dernière mise à jour de l'entité.
	private LocalDateTime updatedAt;
	
	// Identifiant de l'utilisateur ayant créé le livre.
	private String createdBy;
	
	/**** Many To One ****/

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "genre_id", nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Genre genre;

	public Genre getGenre() {
		return genre;
	}

	public void setGenre(Genre Genre) {
		this.genre = genre;
	}
	
	
	

}

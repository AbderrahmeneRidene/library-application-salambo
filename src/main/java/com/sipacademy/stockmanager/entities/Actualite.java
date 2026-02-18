package com.sipacademy.stockmanager.entities;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;

@Entity
@Table(name = "actualite")
public class Actualite {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "actualite_id")
	private long id;

	@Column(name = "title")
	@NotEmpty(message = "*Please provide your title")
	private String title;

	@Column(name = "subtitle")
	private String subtitle;

	@Column(name = "description")
	private String description;

	@Column(name = "lastUpdated")
	private LocalDateTime lastUpdated;

	@Column(name = "photo")
	private String photo;
	// private String username;

	public Actualite() {
	}

	@Override
	public String toString() {
		return "Actualite [id=" + id + ", titre=" + title + ", description=" + description + ", dateCreation="
				+ lastUpdated + ", photo=" + photo + "]";
	}

	public long getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LocalDateTime getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(LocalDateTime lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public String getSubtitle() {
		return subtitle;
	}

	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}

	public String getElapsedTime() {
        if (lastUpdated == null) {
            return "Unknown";
        }

        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(lastUpdated, now);

        long days = duration.toDays();
        long hours = duration.toHours() % 24;
        long minutes = duration.toMinutes() % 60;

        if (days > 0) {
            return days + " day(s)";
        } else if (hours > 0) {
            return hours + " hour(s)";
        } else if (minutes > 0) {
            return minutes + " minute(s)";
        } else {
            return "A few seconds";
        }
    }

	/*
	 * public String getUsername() { return username; }
	 * 
	 * public void setUsername(String username) { this.username = username; }
	 */
}

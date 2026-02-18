package com.sipacademy.stockmanager.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Sitting {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String description;
	private String phoneNumber1;
	private String phoneNumber2;
	private String address;
	private String email;
	private String copyRight;
	private String urlTwitter;
	private String urlFacebook;
	private String urlLinkedin;
	private String urlInstagram;
	private String urlYoutube;
	
	public String getUrlTwitter() {
		return urlTwitter;
	}
	public void setUrlTwitter(String urlTwitter) {
		this.urlTwitter = urlTwitter;
	}
	public String getUrlFacebook() {
		return urlFacebook;
	}
	public void setUrlFacebook(String urlFacebook) {
		this.urlFacebook = urlFacebook;
	}
	public String getUrlLinkedin() {
		return urlLinkedin;
	}
	public void setUrlLinkedin(String urlLinkedin) {
		this.urlLinkedin = urlLinkedin;
	}
	public String getUrlInstagram() {
		return urlInstagram;
	}
	public void setUrlInstagram(String urlInstagram) {
		this.urlInstagram = urlInstagram;
	}
	public String getUrlYoutube() {
		return urlYoutube;
	}
	public void setUrlYoutube(String urlYoutube) {
		this.urlYoutube = urlYoutube;
	}
	public String getCopyRight() {
		return copyRight;
	}
	public void setCopyRight(String copyRight) {
		this.copyRight = copyRight;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getPhoneNumber1() {
		return phoneNumber1;
	}
	public void setPhoneNumber1(String phoneNumber1) {
		this.phoneNumber1 = phoneNumber1;
	}
	public String getPhoneNumber2() {
		return phoneNumber2;
	}
	public void setPhoneNumber2(String phoneNumber2) {
		this.phoneNumber2 = phoneNumber2;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
}

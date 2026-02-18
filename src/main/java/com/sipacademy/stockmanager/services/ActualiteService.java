package com.sipacademy.stockmanager.services;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.io.ObjectOutputStream;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.sipacademy.stockmanager.entities.Actualite;
import com.sipacademy.stockmanager.repositories.ActualiteRepository;

@Service
public class ActualiteService {

	@Autowired
	private ActualiteRepository actualiteRepository;

	private final Path root = Paths.get(System.getProperty("user.dir") + "/src/main/resources/static/uploads");

	public void save(Actualite actualite, MultipartFile newPicture) {
		if (newPicture.isEmpty())
			actualite.setPhoto("imgActualiteDefault.jpg");
		else {
			String newImageName = newPicture.getOriginalFilename().concat(getSaltString());

			try {
				Files.copy(newPicture.getInputStream(), this.root.resolve(newImageName)); // upload de l'image
				actualite.setPhoto(newImageName);
			} catch (Exception e) {
				throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
			}
		}
		actualite.setLastUpdated(LocalDateTime.now());
		actualiteRepository.save(actualite);
	}

	public List<Actualite> list() {
		return (List<Actualite>) actualiteRepository.findAll();
	}

	protected static String getSaltString() {
		String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
		StringBuilder salt = new StringBuilder();
		Random rnd = new Random();
		while (salt.length() < 18) { // length of the random string.
			int index = (int) (rnd.nextFloat() * SALTCHARS.length());
			salt.append(SALTCHARS.charAt(index));
		}
		String saltStr = salt.toString();
		return saltStr;
	}

	public Actualite findActualiteById(int id) {
		return actualiteRepository.findById(id).get();
	}

	public void edit(Actualite newActualite, MultipartFile newPicture, int id) {
		Actualite oldActualite = findActualiteById(id);
		if (!newPicture.isEmpty()) {
			// STEP 1 : delete Old Image from server
			String OldImageName = oldActualite.getPhoto();

			////////
			try {
				File f = new File(this.root + "/" + OldImageName); // file to be delete
				if (f.delete()) // returns Boolean value
				{
					System.out.println(f.getName() + " deleted"); // getting and printing the file name
				} else {
					System.out.println("failed");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			/// STEP 2 : Upload new image to server
			String newImageName;

			newImageName = getSaltString().concat(newPicture.getOriginalFilename());
			try {
				Files.copy(newPicture.getInputStream(), this.root.resolve(newImageName));
				oldActualite.setPhoto(newImageName);
			} catch (Exception e) {
				throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
			}
		}

		oldActualite.setTitle(newActualite.getTitle());
		oldActualite.setSubtitle(newActualite.getSubtitle());
		oldActualite.setDescription(newActualite.getDescription());
		oldActualite.setLastUpdated(LocalDateTime.now());
		actualiteRepository.save(oldActualite);
	}

	public void delete(int id) {
		Actualite actualite = actualiteRepository.findById(id).get();
		if (actualite.getPhoto() != null) {
			// STEP 1 : delete Old Image from server
			String OldImageName = actualite.getPhoto();

			try {
				File f = new File(this.root + "/" + OldImageName); // file to be delete
				if (f.delete()) // returns Boolean value
				{
					System.out.println(f.getName() + " deleted"); // getting and printing the file name
				} else {
					System.out.println("failed");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		actualiteRepository.delete(actualite);
	}

}

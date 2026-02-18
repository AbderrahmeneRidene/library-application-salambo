package com.sipacademy.stockmanager.services;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.sipacademy.stockmanager.entities.Actualite;
import com.sipacademy.stockmanager.entities.Article;
import com.sipacademy.stockmanager.entities.Provider;
import com.sipacademy.stockmanager.repositories.ProviderRepository;

@Service
public class ProviderService {

	@Autowired
	private ProviderRepository providerRepository;

	private final Path root = Paths.get(System.getProperty("user.dir") + "/src/main/resources/static/uploads");

	public List<Provider> listProviders() {
		return (List<Provider>) providerRepository.findAll();
	}

	public void save(Provider provider, MultipartFile newPicture) {
		if (newPicture.isEmpty())
			provider.setPhoto("imgProviderDefault.png");
		else {
			String newImageName = newPicture.getOriginalFilename().concat(getSaltString());

			try {
				Files.copy(newPicture.getInputStream(), this.root.resolve(newImageName)); // upload de l'image
				provider.setPhoto(newImageName);
			} catch (Exception e) {
				throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
			}
		}
		// provider.setLastUpdated(LocalDateTime.now());
		providerRepository.save(provider);
	}

	public Provider findProviderById(int id) {
		return providerRepository.findById(id).orElse(null);
	}

	public void delete(int id) {
		Provider provider = providerRepository.findById(id).get();
		if (provider.getPhoto() != null) {
			// STEP 1 : delete Old Image from server
			String OldImageName = provider.getPhoto();

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
		providerRepository.delete(provider);
	}

	public void edit(Provider newProvider, MultipartFile newPicture, int id) {
		Provider oldProvider = findProviderById(id);
		if (!newPicture.isEmpty()) {
			// STEP 1 : delete Old Image from server
			String OldImageName = oldProvider.getPhoto();

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
				oldProvider.setPhoto(newImageName);
			} catch (Exception e) {
				throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
			}
		}

		oldProvider.setName(newProvider.getName());
		oldProvider.setEmail(newProvider.getEmail());
		oldProvider.setAddress(newProvider.getAddress());
		providerRepository.save(oldProvider);
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

	public List<Article> findArticlesByProvider(int id) {
		return providerRepository.findArticlesByProvider(id);
	}

}

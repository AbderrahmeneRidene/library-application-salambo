package com.sipacademy.stockmanager.services;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.sipacademy.stockmanager.entities.Article;
import com.sipacademy.stockmanager.entities.Provider;
import com.sipacademy.stockmanager.repositories.ArticleRepository;

@Service
public class ArticleService {
	
	@Autowired
	private ArticleRepository articleRepository;

	private final Path root = Paths.get(System.getProperty("user.dir") + "/src/main/resources/static/uploads");

	public List<Article> listArticles(){
		return (List<Article>) articleRepository.findAll();
	}
	
	public void save(Article article, MultipartFile newPicture) {
		if (newPicture.isEmpty())
			article.setPicture("imgDefault.jpg");
		else {
			String newImageName = newPicture.getOriginalFilename().concat(ProviderService.getSaltString());

			try {
				Files.copy(newPicture.getInputStream(), this.root.resolve(newImageName)); // upload de l'image
				article.setPicture(newImageName);
			} catch (Exception e) {
				throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
			}
		}
		//provider.setLastUpdated(LocalDateTime.now());
		articleRepository.save(article);
	}
	
	public void delete(int id) {
		Article article = articleRepository.findById(id).orElse(null);
		if (article.getPicture() != null) {
			// STEP 1 : delete Old Image from server
			String OldImageName = article.getPicture();

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
		articleRepository.delete(article);
	}
	
	public Article findArticleById(int id) {
		return articleRepository.findById(id).orElse(null);
	}
	
	public void edit(Article newArticle, MultipartFile newPicture, int id) {
		Article oldArticle = findArticleById(id);
		if (!newPicture.isEmpty()) {
			// STEP 1 : delete Old Image from server
			String OldImageName = oldArticle.getPicture();

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

			newImageName = ProviderService.getSaltString().concat(newPicture.getOriginalFilename());
			try {
				Files.copy(newPicture.getInputStream(), this.root.resolve(newImageName));
				oldArticle.setPicture(newImageName);
			} catch (Exception e) {
				throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
			}
		}

		oldArticle.setLabel(newArticle.getLabel());
		oldArticle.setPrice(newArticle.getPrice());
		articleRepository.save(oldArticle);
	}
}

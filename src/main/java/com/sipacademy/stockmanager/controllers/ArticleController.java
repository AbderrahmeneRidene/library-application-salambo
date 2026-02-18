package com.sipacademy.stockmanager.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.sipacademy.stockmanager.entities.Article;
import com.sipacademy.stockmanager.entities.Provider;
import com.sipacademy.stockmanager.services.ArticleService;
import com.sipacademy.stockmanager.services.ProviderService;

@Controller
@RequestMapping("/article")
public class ArticleController {

	@Autowired
	private ArticleService articleService;

	@Autowired
	private ProviderService providerService;

	@GetMapping("/list")
	public String toViewListArticles(Model model) {
		model.addAttribute("articles", articleService.listArticles());
		return "dashboard/admin/listArticles";
	}

	@GetMapping("add")
	public String showAddArticleForm(Model model) {
		model.addAttribute("article", new Article());
		model.addAttribute("providers", providerService.listProviders());
		return "dashboard/admin/addArticle";
	}

	@PostMapping("/add")
	public String addArticle(@ModelAttribute("article") Article article, BindingResult bindingResult, Model model,
			@RequestParam(name = "providerId", required = false) int providerId,
			@RequestParam("files") MultipartFile[] files) {
		Provider providerExist = providerService.findProviderById(providerId);
		if (providerExist == null) {
			bindingResult.rejectValue("provider_Id", "error.provider_Id", "ProviderId " + providerId + " not found");
		}
		if (bindingResult.hasErrors()) {
			model.addAttribute("article", article);
			return "/dashboard/admin/addArticle";
		}
		// Logique pour la mise à jour de l'utilisateur
		article.setProvider(providerExist);
		articleService.save(article, files[0]);
		return "redirect:list";
	}

	@GetMapping("/delete/{id}")
	public String deleteArticle(@PathVariable int id, Model model) {
		articleService.delete(id);
		List<Article> articles = articleService.listArticles();
		model.addAttribute("articles", articles);
		return "dashboard/admin/listArticles";
	}

	@GetMapping("/edit/{id}")
	public String toViewEditArticle(@PathVariable int id, Model model) {
		Article article = articleService.findArticleById(id);
		model.addAttribute("article", article);
		model.addAttribute("idProvider", article.getProvider().getId());
		model.addAttribute("providers", providerService.listProviders());
		return "dashboard/admin/editArticle";
	}

	@PostMapping("/edit/{id}")
	public String editArticle(@PathVariable int id, @ModelAttribute("article") Article article,
			BindingResult bindingResult, Model model,
			@RequestParam(name = "providerId", required = false) int providerId,
			@RequestParam("files") MultipartFile[] files) {
		
		
		Article oldArticle = articleService.findArticleById(id);

		if (providerId != 0) {
			article.setProvider(providerService.findProviderById(providerId));
		}

		if (bindingResult.hasErrors()) {
			if (oldArticle != null) {
				article.setPicture(oldArticle.getPicture());
			}
			model.addAttribute("article", article);
			return "/dashboard/admin/editArticle";
		}
		// Logique pour la mise à jour de l'utilisateur
		articleService.edit(article, files[0],id);
		List<Article> articles = articleService.listArticles();
		model.addAttribute("articles", articles);
		return "dashboard/admin/listArticles";
	}

}

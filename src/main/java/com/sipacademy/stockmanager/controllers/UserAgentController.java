package com.sipacademy.stockmanager.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sipacademy.stockmanager.services.ArticleService;
import com.sipacademy.stockmanager.services.ProviderService;
import com.sipacademy.stockmanager.services.UserService;

@Controller
@RequestMapping("/userAgent")
public class UserAgentController {
	
	@Autowired
	private ProviderService providerService;
	
	@Autowired
	private ArticleService articleService;

	@GetMapping("/listProviders")
	public String toViewListProviders(Model model) {
		model.addAttribute("providers", providerService.listProviders());
		return "dashboard/agent/listProviders";
	}

	@GetMapping("/listArticles")
	public String toViewListArticles(Model model) {
		model.addAttribute("articles", articleService.listArticles());
		return "dashboard/agent/listArticles";
	}
	
	@GetMapping("show/{id}")
	public String showProvider(@PathVariable("id") int id, Model model) {

		model.addAttribute("articles", providerService.findArticlesByProvider(id));
		//model.addAttribute("provider", providerService.findProviderById(id));
		return "dashboard/agent/showProvider";
	}

}

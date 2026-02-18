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

import com.sipacademy.stockmanager.entities.Provider;
import com.sipacademy.stockmanager.services.ProviderService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/provider")
public class ProviderController {

	@Autowired
	private ProviderService providerService;

	@GetMapping("/list")
	public String toViewListProviders(Model model) {
		model.addAttribute("providers", providerService.listProviders());
		return "dashboard/admin/listProviders";
	}

	@GetMapping("add")
	public String showAddProviderForm(Model model) {
		model.addAttribute("provider", new Provider());
		return "dashboard/admin/addProvider";
	}

	@PostMapping("/add")
	public String addProvider(@ModelAttribute("provider") Provider provider, BindingResult bindingResult,
			@RequestParam("files") MultipartFile[] files, Model model) {
		if (bindingResult.hasErrors()) {
			model.addAttribute("provider", provider);
			return "/dashboard/admin/addProvider";
		}
		// Logique pour la mise à jour de l'utilisateur
		providerService.save(provider, files[0]);
		return "redirect:list";
	}

	@GetMapping("/delete/{id}")
	public String deleteProvider(@PathVariable int id, Model model) {
		providerService.delete(id);
		List<Provider> providers = providerService.listProviders();
		model.addAttribute("providers", providers);
		return "dashboard/admin/listProviders";
	}
	
	@GetMapping("/edit/{id}")
	public String toViewEditProvider(@PathVariable int id, Model model) {
		Provider provider = providerService.findProviderById(id);
		model.addAttribute("provider", provider);
		return "dashboard/admin/editProvider";
	}

	@PostMapping("/edit/{id}")
	public String editProvider(@PathVariable int id, @ModelAttribute("provider") Provider provider,
			BindingResult bindingResult, @RequestParam("files") MultipartFile[] files, Model model) {

		Provider oldProvider = providerService.findProviderById(id);

		if (bindingResult.hasErrors()) {
			if (oldProvider != null) {
				provider.setPhoto(oldProvider.getPhoto());
			}
			model.addAttribute("provider", provider);
			return "/dashboard/admin/editProvider";
		}

		// Logique pour la mise à jour de l'utilisateur
		providerService.edit(provider, files[0], id);
		List<Provider> providers = providerService.listProviders();
		model.addAttribute("providers", providers);
		return "dashboard/admin/listProviders";
	}
	
	@GetMapping("show/{id}")
	public String showProvider(@PathVariable("id") int id, Model model) {

		model.addAttribute("articles", providerService.findArticlesByProvider(id));
		//model.addAttribute("provider", providerService.findProviderById(id));
		return "dashboard/admin/showProvider";
	}

	

}

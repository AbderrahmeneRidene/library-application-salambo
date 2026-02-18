package com.sipacademy.stockmanager.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sipacademy.stockmanager.entities.Actualite;
import com.sipacademy.stockmanager.entities.Provider;
import com.sipacademy.stockmanager.services.ActualiteService;
import com.sipacademy.stockmanager.services.ProviderService;
import com.sipacademy.stockmanager.services.UserService;

@Controller
public class HomeController {
	
	@Autowired
	private ActualiteService actualiteService;
	
	@Autowired
	private ProviderService providerService;
	
	@GetMapping("/home")
    public String  homeFrontView(Model model) {
		List<Actualite> actualites = actualiteService.list();
		long nbr = actualites.size();
		if (actualites.size() == 0)
			actualites = null;
		model.addAttribute("actualites", actualites);
		model.addAttribute("providersLogo", providerService.listProviders().stream()
                .map(Provider::getPhoto) // Récupère l'attribut photo
                .filter(photo -> photo != null && !photo.isEmpty()) // Filtre les photos nulles ou vides
                .collect(Collectors.toList()));
        return "home";
    }
	
	@GetMapping("/about")
    public String  toAboutView(Model model) {
        return "about";
    }
	
	@GetMapping("/contact")
    public String  toContactView(Model model) {
        return "contact";
    }
	
	@GetMapping("/service")
    public String  toServicesView(Model model) {
        return "service";
    }
}

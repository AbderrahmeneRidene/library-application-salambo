package com.sipacademy.stockmanager.controllers;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sipacademy.stockmanager.entities.Provider;
import com.sipacademy.stockmanager.repositories.UserRepository;
import com.sipacademy.stockmanager.services.ProviderService;


@Controller

@RequestMapping("/login")
public class LoginController {

    public LoginController(UserRepository userRepository) {
		super();
		this.userRepository = userRepository;
	}

	private final UserRepository userRepository;
	
	@Autowired
	private ProviderService providerService;

    @GetMapping
    public String loginForm(Model model) {
    	model.addAttribute("providersLogo", providerService.listProviders().stream()
                .map(Provider::getPhoto) // Récupère l'attribut photo
                .filter(photo -> photo != null && !photo.isEmpty()) // Filtre les photos nulles ou vides
                .collect(Collectors.toList()));

        return "login";

    }
/*
    @PostMapping("")
    public String loginSubmit(Model model, @RequestParam String email, @RequestParam String password) {
        User user = userRepository.findByEmail(email);

        if (user!=null) {
                return "redirect:/home";
        }
        model.addAttribute("error", "Invalid email or password");
        return "login";
    }*/
    
    
}

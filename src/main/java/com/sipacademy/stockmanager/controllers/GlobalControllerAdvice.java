package com.sipacademy.stockmanager.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.sipacademy.stockmanager.entities.Actualite;
import com.sipacademy.stockmanager.entities.Provider;
import com.sipacademy.stockmanager.entities.Sitting;
import com.sipacademy.stockmanager.entities.User;
import com.sipacademy.stockmanager.services.ActualiteService;
import com.sipacademy.stockmanager.services.ProviderService;
import com.sipacademy.stockmanager.services.SittingService;
import com.sipacademy.stockmanager.services.UserService;

@ControllerAdvice
public class GlobalControllerAdvice {
	@Autowired
	private UserService userService;


	@Autowired
	private SittingService sittingService;

	@ModelAttribute
	public void addUserToModel(@AuthenticationPrincipal UserDetails userDetails, Model model) {

		Sitting sitting = sittingService.getSittingInfo();
        model.addAttribute("sitting", sitting);
        
		if (userDetails != null) {
			User user = userService.findUserByEmail(userDetails.getUsername());
			model.addAttribute("userName", capitalizeFirstLetter(user.getName()));
			model.addAttribute("userLastName", capitalizeFirstLetter(user.getLastName()));
			model.addAttribute("userPicture", user.getPicture());
		}
	}

	public static String capitalizeFirstLetter(String input) {
		if (input == null || input.isEmpty()) {
			return input;
		}

		// Mettre la première lettre en majuscule et le reste en minuscule
		return input.substring(0, 1).toUpperCase() + input.substring(1).toLowerCase();
	}
}

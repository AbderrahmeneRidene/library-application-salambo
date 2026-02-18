package com.sipacademy.stockmanager.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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

import com.sipacademy.stockmanager.entities.User;
import com.sipacademy.stockmanager.services.UserService;

@Controller
@RequestMapping("/profile")
public class ProfileController {

	@Autowired
	private UserService userService;

	@GetMapping("/edit")
	public String toViewEditAdmin(Model model, @AuthenticationPrincipal UserDetails userDetails) {
		// Logique pour récupérer l'utilisateur par son ID et le passer au modèle
		User user = userService.findUserByEmail(userDetails.getUsername());
		if (user.getRoles().stream().anyMatch(roleToCheck -> "SUPERADMIN".equals(roleToCheck.getRole()))) {
			model.addAttribute("user", user);
			return "/dashboard/superAdmin/editConnectedProfile";
		} else if (user.getRoles().stream().anyMatch(roleToCheck -> "ADMIN".equals(roleToCheck.getRole()))) {
			model.addAttribute("user", user);
			return "/dashboard/admin/editConnectedProfile";
		} else if (user.getRoles().stream().anyMatch(roleToCheck -> "AGENT".equals(roleToCheck.getRole()))) {
			model.addAttribute("user", user);
			return "/dashboard/agent/editConnectedProfile";
		} else
			return "/logout";

	}

	@PostMapping("/edit")
	public String updateAdmin(@ModelAttribute("user") User user, BindingResult bindingResult,
			@RequestParam("files") MultipartFile[] files, Model model,
			@AuthenticationPrincipal UserDetails userDetails) {

		User userExists = userService.findUserByEmail(userDetails.getUsername());
		User otherUserWithSameEmail = userService.findUserByEmail(user.getEmail());
		if (otherUserWithSameEmail != null && otherUserWithSameEmail.getId() != userExists.getId()) {
			bindingResult.rejectValue("email", "error.user",
					"There is already a user registered with the email provided");
			user.setPicture(userExists.getPicture());
		}
		boolean reject = bindingResult.hasErrors();

		if (userExists.getRoles().stream().anyMatch(roleToCheck -> "SUPERADMIN".equals(roleToCheck.getRole()))) {
			if (reject) {
				model.addAttribute("user", user);
				return "/dashboard/superAdmin/editConnectedProfile";
			} else {
				userService.updateUser(user, userExists.getId(), files[0]);
				return "redirect:../homeSuperAdmin";
			}
		} else if (userExists.getRoles().stream().anyMatch(roleToCheck -> "ADMIN".equals(roleToCheck.getRole()))) {
			if (reject) {
				model.addAttribute("user", user);
				return "/dashboard/admin/editConnectedProfile";
			} else {
				userService.updateUser(user, userExists.getId(), files[0]);
				return "redirect:../homeAdmin";
			}
		} else if (userExists.getRoles().stream().anyMatch(roleToCheck -> "AGENT".equals(roleToCheck.getRole()))) {
			if (reject) {
				model.addAttribute("user", user);
				return "/dashboard/agent/editConnectedProfile";
			} else {
				userService.updateUser(user, userExists.getId(), files[0]);
				return "redirect:../homeAgent";
			}
		} else
			return "/logout";
	}

}

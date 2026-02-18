package com.sipacademy.stockmanager.controllers;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.sipacademy.stockmanager.services.UserService;

import jakarta.servlet.http.HttpSession;

import com.sipacademy.stockmanager.entities.User;

@Controller
@RequestMapping("/admin/")
public class CrudAdminController {

	@Autowired
	private UserService userService;

	@GetMapping("list")
	public String listUsers(Model model) {

		List<User> users = (List<User>) userService.listUsers("ADMIN");
		long nbr = users.size();
		if (users.size() == 0)
			users = null;
		model.addAttribute("users", users);
		model.addAttribute("nbr", nbr);
		return "dashboard/superAdmin/listAdmin";
	}

	@RequestMapping("add")
	public String addAdminForm(Model model) {
		model.addAttribute("user", new User());
		return "/dashboard/superAdmin/addAdmin";
	}

	@RequestMapping("save")
	public String Register(@ModelAttribute("user") User user, BindingResult bindingResult,
			@RequestParam("files") MultipartFile[] files) {
		ModelAndView modelAndView = new ModelAndView();
		User userExists = userService.findUserByEmail(user.getEmail());
		if (userExists != null) {
			bindingResult.rejectValue("email", "error.user",
					"There is already a user registered with the email provided");
		}
		if (bindingResult.hasErrors()) {
			return "/dashboard/superAdmin/addAdmin";
		} else {
			userService.saveUsers(user, files[0], "ADMIN");
			modelAndView.addObject("successMessage", "User has been registered successfully");
			modelAndView.addObject("user", new User());
			modelAndView.setViewName("/dashboard/superAdmin/listAdmin");
		}
		return "redirect:list";
	}

	@GetMapping("enable/{id}/{email}")
	public String enableUserAcount(@PathVariable("id") int id, @PathVariable("email") String email) {
		userService.enableAccount(id, email);
		return "redirect:../../list";
	}

	@GetMapping("disable/{id}/{email}")
	public String disableUserAcount(@PathVariable("id") int id, @PathVariable("email") String email) {
		userService.disableAccount(id, email);
		return "redirect:../../list";
	}

	@GetMapping("searchByAnything")
	public String searchArticleByLabel(@RequestParam("nameSearched") String nameSearched, Model model) {

		List<User> users;
		if (nameSearched != null && !nameSearched.isEmpty())
			users = ((List<User>) userService.listUsers("ADMIN")).stream()
					.filter(user -> user.getEmail().toLowerCase().contains(nameSearched)
							|| user.getName().toLowerCase().contains(nameSearched)
							|| user.getLastName().toLowerCase().contains(nameSearched)
							|| user.getId() == safeParseInt(nameSearched, 0))
					.collect(Collectors.toList());
		else
			users = (List<User>) userService.listUsers("ADMIN");
		model.addAttribute("users", users);

		return "dashboard/superAdmin/listAdmin";
	}

	@GetMapping("edit/{id}")
	public String showUserFormToUpdate(@PathVariable int id, Model model) {
		// Logique pour récupérer l'utilisateur par son ID et le passer au modèle
		User user = userService.findUserById(id);
		model.addAttribute("user", user);
		return "/dashboard/superAdmin/editAdmin"; // Retourne la vue d'édition de l'utilisateur
	}

	@PostMapping("edit/{id}")
	public String updateUser(
	        @PathVariable int id,
	        @ModelAttribute("user") User user,
	        BindingResult bindingResult,
	        @RequestParam("files") MultipartFile[] files,
	        Model model) {

	    User userExists = userService.findUserById(id);

	    // Vérifiez si l'email appartient déjà à un autre utilisateur
	    User otherUserWithSameEmail = userService.findUserByEmail(user.getEmail());
	    if (otherUserWithSameEmail != null && otherUserWithSameEmail.getId() != id) {
	        bindingResult.rejectValue("email", "error.user", "There is already a user registered with the email provided");

	        // S'assurer que l'ancienne image est toujours disponible
	        user.setPicture(userExists.getPicture());

	        model.addAttribute("user", user);
	        return "/dashboard/superAdmin/editAdmin";
	    }

	    // Logique pour la mise à jour de l'utilisateur
	    userService.updateUser(user,id, files[0]);
	    return "redirect:../list";
	}


	
	private String saveTemporaryFile(MultipartFile file) {
	    String tempDirectory = System.getProperty("java.io.tmpdir");
	    String tempFilePath = tempDirectory + "/" + file.getOriginalFilename();
	    try {
	        file.transferTo(new File(tempFilePath));
	    } catch (IOException e) {
	        //e.printStackTrace();
	    }
	    return tempFilePath;
	}

	public static int safeParseInt(String str, int defaultVal) {
		if (str == null || str.isEmpty()) {
			return defaultVal;
		}
		try {
			return Integer.parseInt(str);
		} catch (NumberFormatException e) {
			return defaultVal;
		}
	}

}

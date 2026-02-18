package com.sipacademy.stockmanager.controllers;

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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.sipacademy.stockmanager.entities.Role;
import com.sipacademy.stockmanager.entities.User;
import com.sipacademy.stockmanager.services.RoleService;
import com.sipacademy.stockmanager.services.UserService;

@Controller
@RequestMapping("/superAdmin")
public class CrudSuperAdminController {

	@Autowired
	private UserService userService;

	@Autowired
	private RoleService roleService;

	@GetMapping("/list")
	public String listUsers(Model model) {

		List<User> users = (List<User>) userService.listUsers("SUPERADMIN");
		long nbr = users.size();
		if (users.size() == 0)
			users = null;
		model.addAttribute("users", users);
		model.addAttribute("nbr", nbr);
		return "dashboard/crudSuperAdmin/listSuperAdmin";
	}

	@RequestMapping("/add")
	public String addSuperAdminForm(Model model) {
		model.addAttribute("user", new User());
		return "/dashboard/crudSuperAdmin/addSuperAdmin";
	}

	@RequestMapping("/save")
	public String Register(@ModelAttribute("user") User user, BindingResult bindingResult,
			@RequestParam("files") MultipartFile[] files) {
		ModelAndView modelAndView = new ModelAndView();
		User userExists = userService.findUserByEmail(user.getEmail());
		if (userExists != null) {
			bindingResult.rejectValue("email", "error.user",
					"There is already a user registered with the email provided");
		}
		if (bindingResult.hasErrors()) {
			return "/dashboard/crudSuperAdmin/addSuperAdmin";
		} else {
			userService.saveUsers(user, files[0], "SUPERADMIN");
			modelAndView.addObject("successMessage", "User has been registered successfully");
			modelAndView.addObject("user", new User());
			modelAndView.setViewName("dashboard/crudSuperAdmin/listSuperAdmin");
		}
		return "redirect:list";
	}

	@GetMapping("/edit/{id}")
	public String showUserFormToUpdate(@PathVariable int id, Model model) {
		// Logique pour récupérer l'utilisateur par son ID et le passer au modèle
		User user = userService.findUserById(id);
		model.addAttribute("user", user);
		return "/dashboard/crudSuperAdmin/editSuperAdmin"; // Retourne la vue d'édition de l'utilisateur
	}

	@PostMapping("/edit/{id}")
	public String updateUser(@PathVariable int id, @ModelAttribute("user") User user, BindingResult bindingResult,
			@RequestParam("files") MultipartFile[] files, Model model) {

		User userExists = userService.findUserById(id);

		// Vérifiez si l'email appartient déjà à un autre utilisateur
		User otherUserWithSameEmail = userService.findUserByEmail(user.getEmail());
		if (otherUserWithSameEmail != null && otherUserWithSameEmail.getId() != id) {
			bindingResult.rejectValue("email", "error.user",
					"There is already a user registered with the email provided");

			// S'assurer que l'ancienne image est toujours disponible
			user.setPicture(userExists.getPicture());

			model.addAttribute("user", user);
			return "/dashboard/crudSuperAdmin/editSuperAdmin";
		}

		// Logique pour la mise à jour de l'utilisateur
		userService.updateUser(user, id, files[0]);
		return "redirect:../list";
	}

	@GetMapping("/listRoles")
	public String listroles(Model model) {
		List<Role> roles = roleService.listRoles();
		long nbr = roles.size();
		if (roles.size() == 0)
			roles = null;
		else {
			for (Role role : roles) {
				role.setNbrUsers(userService.listUsers(role.getRole()).size());
			}
		}
		model.addAttribute("roles", roles);
		model.addAttribute("role", new Role());
		return "dashboard/crudSuperAdmin/listRoles";
	}

	@GetMapping("/listUserRole")
	public String listUserRole(Model model) {
		List<User> users = userService.listUsers("ALL");
		long nbr = users.size();
		if (users.size() == 0)
			users = null;
		model.addAttribute("users", users);
		model.addAttribute("nbr", nbr);
		return "dashboard/crudSuperAdmin/listUserRole";
	}

	@GetMapping("/addRole")
	public String toaAddRoleView(Model model) {
		model.addAttribute(new Role());
		return "/dashboard/crudSuperAdmin/addRole";
	}

	@PostMapping("/addRole")
	public String Register(@RequestParam("role") String role, Model model) {
		Role newRole = new Role(role);
		Role roleExists = roleService.findRoleByRole(newRole.getRole().toUpperCase());
		newRole.setRole(newRole.getRole().toUpperCase());

		if (roleExists == null && isValidRole(newRole.getRole())) {
			roleService.addRole(newRole);
			return "redirect:listRoles";
		} else {

			model.addAttribute("role", newRole);
			return "/dashboard/crudSuperAdmin/addRole";
		}
	}

	@PostMapping("updateRole")
	// @ResponseBody
	public String UpdateUserRole(@RequestParam("id") int id, @RequestParam("newrole") String newRole, Model model) {
		if (newRole.equals("SUPERADMIN")||newRole.equals("ADMIN")||newRole.equals("AGENT"))
			userService.updateRole(id, newRole);
		model.addAttribute("users", userService.listUsers("ALL"));
		return "dashboard/crudSuperAdmin/listUserRole";
	}

	@GetMapping("searchByAnything")
	public String searchArticleByLabel(@RequestParam("nameSearched") String nameSearched, Model model) {

		List<User> users;
		if (nameSearched != null && !nameSearched.isEmpty())
			users = ((List<User>) userService.listUsers("ALL")).stream()
					.filter(user -> user.getEmail().toLowerCase().contains(nameSearched)
							|| user.getName().toLowerCase().contains(nameSearched)
							|| user.getLastName().toLowerCase().contains(nameSearched)
							|| user.getId() == safeParseInt(nameSearched, 0))
					.collect(Collectors.toList());
		else
			users = (List<User>) userService.listUsers("ALL");
		model.addAttribute("users", users);

		return "dashboard/crudSuperAdmin/listUserRole";
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

	public static boolean isValidRole(String input) {
		// Vérifier si la chaîne n'est pas nulle ou vide
		if (input == null || input.isEmpty()) {
			return false;
		}
		// Utiliser une expression régulière pour vérifier les caractères
		return input.matches("[a-zA-Z]+");
	}

}

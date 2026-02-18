package com.sipacademy.stockmanager.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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
@RequestMapping("/role")
public class RoleController {

	@Autowired
	private RoleService roleService;

	@Autowired
	private UserService userService;

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
		return "dashboard/superAdmin/listRoles";
	}

	@GetMapping("/listUserRole")
	public String listUserRole(Model model) {
		List<User> users = userService.listUsers("WithoutSUPERADMIN");
		long nbr = users.size();
		if (users.size() == 0)
			users = null;
		model.addAttribute("users", users);
		model.addAttribute("nbr", nbr);
		return "dashboard/superAdmin/listUserRole";
	}

	/*
	@GetMapping("/add")
	public String toaAddRoleView(Model model) {
		model.addAttribute(new Role());
		return "dashboard/superAdmin/addRole";
	}

	@RequestMapping("/add")
	public String Register(@RequestParam("role") String role, Model model) {
		Role newRole = new Role(role);
		Role roleExists = roleService.findRoleByRole(newRole.getRole().toUpperCase());
		newRole.setRole(newRole.getRole().toUpperCase());

		if (roleExists == null && isValidRole(newRole.getRole())) {
			roleService.addRole(newRole);
			return "redirect:listRoles";
		} else {
			
			model.addAttribute("role", newRole);
			return "/dashboard/superAdmin/addRole";
		}
	}*/

	@PostMapping("updateRole")
	// @ResponseBody
	public String UpdateUserRole(@RequestParam("id") int id, @RequestParam("newrole") String newRole, Model model) {
		if (newRole.equals("ADMIN")||newRole.equals("AGENT")) userService.updateRole(id, newRole);
		model.addAttribute("users", userService.listUsers("WithoutSUPERADMIN"));
		return "dashboard/superAdmin/listUserRole";
	}

	@GetMapping("searchByAnything")
	public String searchArticleByLabel(@RequestParam("nameSearched") String nameSearched, Model model) {

		List<User> users;
		if (nameSearched != null && !nameSearched.isEmpty())
			users = ((List<User>) userService.listUsers("WithoutSUPERADMIN")).stream()
					.filter(user -> user.getEmail().toLowerCase().contains(nameSearched)
							|| user.getName().toLowerCase().contains(nameSearched)
							|| user.getLastName().toLowerCase().contains(nameSearched)
							|| user.getId() == safeParseInt(nameSearched, 0))
					.collect(Collectors.toList());
		else
			users = (List<User>) userService.listUsers("WithoutSUPERADMIN");
		model.addAttribute("users", users);

		return "dashboard/superAdmin/listUserRole";
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

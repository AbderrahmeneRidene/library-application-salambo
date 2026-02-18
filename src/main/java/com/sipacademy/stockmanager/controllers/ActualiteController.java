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

import com.sipacademy.stockmanager.entities.Actualite;
import com.sipacademy.stockmanager.entities.User;
import com.sipacademy.stockmanager.services.ActualiteService;

@Controller
@RequestMapping("/actualite")
public class ActualiteController {
	
	@Autowired
	private ActualiteService actualiteService;
	
	@GetMapping("/add")
	public String toViewAddActualite(Model model) {
		model.addAttribute("actualite", new Actualite());
		return "dashboard/superAdmin/addActualite";
	}
	
	@PostMapping("/add")
	public String addActualite(
	        @ModelAttribute("actualite") Actualite actualite,
	        BindingResult bindingResult,
	        @RequestParam("files") MultipartFile[] files,
	        Model model) {

	    if (!isAlphaNumeric(actualite.getTitle())) {
	        bindingResult.rejectValue("title", "error.title", "This title must contain only letters and spaces");
	    } else if (!isNullOrAlphaNumeric(actualite.getSubtitle())) {
	    	bindingResult.rejectValue("subtitle", "error.subtitle", "This subtitle must contain only letters and spaces");
	    } else if (!isNullOrAlphaNumeric(actualite.getDescription())) {
	    	bindingResult.rejectValue("description", "error.description", "This description must contain only letters and spaces");
	    }
	    if(bindingResult.hasErrors()) {
	    	model.addAttribute("actualite", actualite);
	        return "/dashboard/superAdmin/addActualite";
	    }

	    // Logique pour la mise à jour de l'utilisateur
	    actualiteService.save(actualite, files[0]);
	    return "redirect:list";
	}
	
	@GetMapping("/list")
	public String listActualite(Model model) {
		List<Actualite> actualites = actualiteService.list();
		long nbr = actualites.size();
		if (actualites.size() == 0)
			actualites = null;
		model.addAttribute("actualites", actualites);
		model.addAttribute("nbr", nbr);
		return "dashboard/superAdmin/listActualite";
	}
	
	@GetMapping("/delete/{id}")
	public String deleteActualite(@PathVariable int id, Model model) {
		actualiteService.delete(id);
		List<Actualite> actualites = actualiteService.list();
		model.addAttribute("actualites", actualites);
		return "dashboard/superAdmin/listActualite";
	}
	
	@GetMapping("/edit/{id}")
	public String toViewEditActualite(@PathVariable int id, Model model) {
		Actualite actualite = actualiteService.findActualiteById(id);
		model.addAttribute("actualite", actualite);
		return "dashboard/superAdmin/editActualite";
	}
	
	
	
	@PostMapping("/edit/{id}")
	public String editActualite(
			@PathVariable int id,
	        @ModelAttribute("actualite") Actualite actualite,
	        BindingResult bindingResult,
	        @RequestParam("files") MultipartFile[] files,
	        Model model) {
		
		Actualite oldActualite = actualiteService.findActualiteById(id);

	    if (!isAlphaNumeric(actualite.getTitle())) {
	        bindingResult.rejectValue("title", "error.title", "This title must contain only letters and spaces");
	    } else if (!isNullOrAlphaNumeric(actualite.getSubtitle())) {
	    	bindingResult.rejectValue("subtitle", "error.subtitle", "This subtitle must contain only letters and spaces");
	    } else if (!isNullOrAlphaNumeric(actualite.getDescription())) {
	    	bindingResult.rejectValue("description", "error.description", "This description must contain only letters and spaces");
	    }
	    if(bindingResult.hasErrors()) {
	    	if(oldActualite != null) {
	    		actualite.setPhoto(oldActualite.getPhoto());
	    	}
	    	model.addAttribute("actualite", actualite);
	        return "/dashboard/superAdmin/editActualite";
	    }

	    // Logique pour la mise à jour de l'utilisateur
	    actualiteService.edit(actualite, files[0],id);
	    List<Actualite> actualites = actualiteService.list();
		model.addAttribute("actualites", actualites);
		return "dashboard/superAdmin/listActualite";
	}
	
	public static boolean isAlphaNumeric(String input) {
        if (input == null || input.isEmpty()) {
            return false; // Retourne false si la chaîne est null ou vide
        }
        return input.matches("[a-zA-Z0-9 ]+"); // Vérifie que la chaîne ne contient que des lettres ou des espaces
    }
	
	public static boolean isNullOrAlphaNumeric(String str) {
        if (str == null) {
            return true;
        }
        return str.matches("[a-zA-Z0-9\\s]*");
    }

}

package com.sipacademy.stockmanager.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sipacademy.stockmanager.entities.Sitting;
import com.sipacademy.stockmanager.services.SittingService;

@Controller
@RequestMapping("/sitting")
public class SittingController {
	
	@Autowired
	private SittingService sittingService;

	@GetMapping("/show")
    public String showFooter(Model model) {
        return "/dashboard/superAdmin/showSitting";
    }
	
	@GetMapping("/edit")
    public String editFooterPage(Model model) {
        return "/dashboard/superAdmin/editSitting";
    }

    @PostMapping("/edit")
    public String updateFooter(Sitting sitting) {
    	sittingService.updateFooterInfo(sitting);
        return "redirect:show"; // Redirige vers la page d'accueil
    }
}

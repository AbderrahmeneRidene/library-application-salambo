package com.sipacademy.stockmanager.controllers;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class UsersHomeController {
	

    @GetMapping("/homeSuperAdmin")
    public String  homeSuperAdmin(Model model) {
        return "dashboard/superAdmin/home";
    }
    
    @GetMapping("/homeAdmin")
    public String  homeAdmin(Model model) {
        return "dashboard/admin/home";
    }
    
    @GetMapping("/homeAgent")
    public String  homeAgent(Model model) {
        return "dashboard/agent/home";
    }
}

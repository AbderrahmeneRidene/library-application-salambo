package com.sipacademy.stockmanager.configuration;


import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // Récupérer les rôles de l'utilisateur
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        String redirectUrl = "/default";
        for (GrantedAuthority authority : authorities) {
            if (authority.getAuthority().equals("SUPERADMIN")) {
                redirectUrl = "/homeSuperAdmin";
                break;
            } else if (authority.getAuthority().equals("ADMIN")) {
                redirectUrl = "/homeAdmin";
                break;
            } else if (authority.getAuthority().equals("AGENT")) {
                redirectUrl = "/homeAgent";
                break;
            }
        }

        response.sendRedirect(redirectUrl);
    }
}


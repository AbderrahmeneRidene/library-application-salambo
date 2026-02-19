package com.sipacademy.stockmanager.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.boot.CommandLineRunner;

@Configuration
public class PasswordConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Vérifie que le PasswordEncoder est bien chargé sur Azure
    @Bean
    public CommandLineRunner testPasswordEncoder(PasswordEncoder encoder) {
        return args -> System.out.println("PasswordEncoder is loaded: " + encoder.encode("test"));
    }
}

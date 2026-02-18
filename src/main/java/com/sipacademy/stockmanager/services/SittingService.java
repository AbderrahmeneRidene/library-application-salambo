package com.sipacademy.stockmanager.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sipacademy.stockmanager.entities.Sitting;
import com.sipacademy.stockmanager.repositories.SittingRepository;

@Service
public class SittingService {
	
	@Autowired
	private SittingRepository sittingRepository;
	
	public Sitting getSittingInfo() {
        if (sittingRepository.count() == 0) { // Si aucune donnée, insère des données par défaut
        	Sitting defaultSitting = new Sitting();
        	defaultSitting.setEmail("default@example.com");
        	defaultSitting.setDescription("Ceci est une description par défaut.");
        	defaultSitting.setPhoneNumber1("000-000-0001");
        	defaultSitting.setPhoneNumber2("000-000-0002");
        	defaultSitting.setAddress("Adresse par défaut");
        	defaultSitting.setCopyRight("Copyright © Designed & Developed by SIP Academy 2024");
        	defaultSitting.setUrlFacebook("#");
        	defaultSitting.setUrlInstagram("#");
        	defaultSitting.setUrlLinkedin("#");
        	defaultSitting.setUrlTwitter("#");
        	defaultSitting.setUrlYoutube("#");
        	sittingRepository.save(defaultSitting);
        }
        return sittingRepository.findAll().get(0);
    }

    public void updateFooterInfo(Sitting sitting) {
    	sittingRepository.save(sitting);
    }

}

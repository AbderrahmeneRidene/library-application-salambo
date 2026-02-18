package com.sipacademy.stockmanager.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sipacademy.stockmanager.entities.Role;
import com.sipacademy.stockmanager.entities.User;
import com.sipacademy.stockmanager.repositories.RoleRepository;

@Service
public class RoleService {

	@Autowired // Spring va se charger de créer un objet qui implémente cette interface
	RoleRepository roleRepository;

	public Role addRole(Role role) {
		return roleRepository.save(role); // sauvegarder dans la base
	}

	public List<Role> listRoles() {
		return (List<Role>) roleRepository.findAll(); // lister tous les roles de la base
	}
	
	public Role findRoleByRole(String role) {
		return roleRepository.findByRole(role);
	}
}

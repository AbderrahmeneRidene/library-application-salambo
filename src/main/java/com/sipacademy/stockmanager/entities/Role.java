package com.sipacademy.stockmanager.entities;

import jakarta.persistence.Column;
import org.springframework.security.core.GrantedAuthority;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "role")
public class Role implements GrantedAuthority {
    
	@Override
	public String toString() {
		return "role = " + role;
	}

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private int id;
	
    @Column(name = "role")
    private String role;

    @Column(name = "nbrUsers")
    private int nbrUsers = 0;
    
	public int getNbrUsers() {
		return nbrUsers;
	}
	public void setNbrUsers(int nbrUsers) {
		this.nbrUsers = nbrUsers;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	
	public Role( String role) {
		super();
		this.role = role;
	}
	
	public Role() {
		super();
	}
	
	@Override
    public String getAuthority() {
        return "ROLE_" + role;
    }
}

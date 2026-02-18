package com.sipacademy.stockmanager.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.sipacademy.stockmanager.entities.Role;
import com.sipacademy.stockmanager.entities.User;
import com.sipacademy.stockmanager.repositories.RoleRepository;
import com.sipacademy.stockmanager.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service("userService")
public class UserService {

	private UserRepository userRepository;
	private RoleRepository roleRepository;
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	private final Path root = Paths.get(System.getProperty("user.dir") + "/src/main/resources/static/uploads");

	@Autowired
	private JavaMailSender javaMailSender;

	public List<User> listUsers(String role) {

		if (role.equals("ALL"))
			return (List<User>) userRepository.findAll();
		else if (role.equals("WithoutSUPERADMIN")) {
			return
					(List<User>) userRepository.findAll().stream().filter(
							user -> user.getRoles().stream().anyMatch(roleToCheck -> !"SUPERADMIN".equals(roleToCheck.getRole())))
							.collect(Collectors.toList());
		} else
			return (List<User>) userRepository.findAll().stream().filter(
					user -> user.getRoles().stream().anyMatch(roleToCheck -> role.equals(roleToCheck.getRole())))
					.collect(Collectors.toList()); // lister tous les roles de la base
	}

	@Autowired
	public UserService(UserRepository userRepository, RoleRepository roleRepository,
			BCryptPasswordEncoder bCryptPasswordEncoder) {
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}

	public User findUserByEmail(String email) {
		return userRepository.findByEmail(email).orElse(null);
	}

	public void saveUsers(User user, MultipartFile newPicture, String role) {
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		if (role.equals("SUPERADMIN")) user.setActive(1); else user.setActive(0);
		Role userRole = roleRepository.findByRole(role);
		user.setRoles(new HashSet<Role>(Arrays.asList(userRole)));
		if (newPicture.isEmpty())
			user.setPicture("inconnu.jpg");
		else {
			String newImageName = getSaltString().concat(newPicture.getOriginalFilename());

			try {
				Files.copy(newPicture.getInputStream(), this.root.resolve(newImageName)); // upload de l'image
				user.setPicture(newImageName);
			} catch (Exception e) {
				throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
			}
		}
		userRepository.save(user);
	}

	protected static String getSaltString() {
		String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
		StringBuilder salt = new StringBuilder();
		Random rnd = new Random();
		while (salt.length() < 18) { // length of the random string.
			int index = (int) (rnd.nextFloat() * SALTCHARS.length());
			salt.append(SALTCHARS.charAt(index));
		}
		String saltStr = salt.toString();
		return saltStr;

	}

	public User enableAccount(int id, String email) {
		sendEmail(email, true);
		User user = userRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid User Id:" + id));
		user.setActive(1);
		return userRepository.save(user);
	}

	public User disableAccount(int id, String email) {
		sendEmail(email, false);
		User user = userRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid User Id:" + id));
		user.setActive(0);
		return userRepository.save(user);
	}

	void sendEmail(String email, boolean state) {

		SimpleMailMessage msg = new SimpleMailMessage();
		msg.setTo(email);
		if (state == true) {
			msg.setSubject("Account Has Been Activated");
			msg.setText("Hello, Your account has been activated. " + "You can log in : http://127.0.0.1:81/login"
					+ " \n Best Regards!");
		} else {
			msg.setSubject("Account Has Been disactivated");
			msg.setText("Hello, Your account has been disactivated.");
		}
		javaMailSender.send(msg);

	}

	public User findUserById(int id) {
		return userRepository.findById(id).get();
	}

	public void updateUser(User newUser, int id, MultipartFile newPicture) {
		User oldUser = findUserById(id);
		if (!newPicture.isEmpty()) {
			// STEP 1 : delete Old Image from server
			String OldImageName = oldUser.getPicture();

			////////
			try {
				File f = new File(this.root + "/" + OldImageName); // file to be delete
				if (f.delete()) // returns Boolean value
				{
					System.out.println(f.getName() + " deleted"); // getting and printing the file name
				} else {
					System.out.println("failed");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			/// STEP 2 : Upload new image to server
			String newImageName;

			newImageName = getSaltString().concat(newPicture.getOriginalFilename());
			try {
				Files.copy(newPicture.getInputStream(), this.root.resolve(newImageName));
				oldUser.setPicture(newImageName);
			} catch (Exception e) {
				throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
			}
		}

		oldUser.setEmail(newUser.getEmail());
		oldUser.setName(newUser.getName());
		oldUser.setLastName(newUser.getLastName());
		userRepository.save(oldUser);
	}
	
	public void updateRole(int id, String newRole) {
		User user = userRepository.findById(id).orElseThrow(()->new IllegalArgumentException("Invalid User Id:" + id));
	     
		 Role userRole = roleRepository.findByRole(newRole);
		 
	     user.setRoles(new HashSet<Role>(Arrays.asList(userRole)));
	     
	     userRepository.save(user);
		
	}
}
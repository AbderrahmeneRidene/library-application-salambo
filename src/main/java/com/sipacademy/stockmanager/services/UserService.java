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
import org.springframework.security.crypto.password.PasswordEncoder;

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

	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final PasswordEncoder passwordEncoder;
	private final Path root = Paths.get(System.getProperty("user.dir") + "/src/main/resources/static/uploads");

	@Autowired
	private JavaMailSender javaMailSender;

	@Autowired
	public UserService(UserRepository userRepository, RoleRepository roleRepository,
					   PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.passwordEncoder = passwordEncoder;
	}

	public List<User> listUsers(String role) {
		if (role.equals("ALL")) {
			return (List<User>) userRepository.findAll();
		} else if (role.equals("WithoutSUPERADMIN")) {
			return userRepository.findAll().stream()
					.filter(user -> user.getRoles().stream()
							.anyMatch(r -> !"SUPERADMIN".equals(r.getRole())))
					.collect(Collectors.toList());
		} else {
			return userRepository.findAll().stream()
					.filter(user -> user.getRoles().stream()
							.anyMatch(r -> role.equals(r.getRole())))
					.collect(Collectors.toList());
		}
	}

	public User findUserByEmail(String email) {
		return userRepository.findByEmail(email).orElse(null);
	}

	public void saveUsers(User user, MultipartFile newPicture, String role) {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user.setActive("SUPERADMIN".equals(role) ? 1 : 0);
		Role userRole = roleRepository.findByRole(role);
		user.setRoles(new HashSet<>(Arrays.asList(userRole)));

		if (newPicture.isEmpty()) {
			user.setPicture("inconnu.jpg");
		} else {
			String newImageName = getSaltString() + newPicture.getOriginalFilename();
			try {
				Files.copy(newPicture.getInputStream(), this.root.resolve(newImageName));
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
		while (salt.length() < 18) {
			int index = (int) (rnd.nextFloat() * SALTCHARS.length());
			salt.append(SALTCHARS.charAt(index));
		}
		return salt.toString();
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

	private void sendEmail(String email, boolean state) {
		SimpleMailMessage msg = new SimpleMailMessage();
		msg.setTo(email);
		if (state) {
			msg.setSubject("Account Has Been Activated");
			msg.setText("Hello, Your account has been activated. You can log in : http://127.0.0.1:81/login\nBest Regards!");
		} else {
			msg.setSubject("Account Has Been Disactivated");
			msg.setText("Hello, Your account has been disactivated.");
		}
		javaMailSender.send(msg);
	}

	public User findUserById(int id) {
		return userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid User Id:" + id));
	}

	public void updateUser(User newUser, int id, MultipartFile newPicture) {
		User oldUser = findUserById(id);

		if (!newPicture.isEmpty()) {
			// Delete old image
			String oldImageName = oldUser.getPicture();
			try {
				File f = new File(this.root + "/" + oldImageName);
				if (f.exists() && !f.delete()) {
					System.out.println("Failed to delete old file: " + oldImageName);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			// Upload new image
			String newImageName = getSaltString() + newPicture.getOriginalFilename();
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
		User user = findUserById(id);
		Role userRole = roleRepository.findByRole(newRole);
		user.setRoles(new HashSet<>(Arrays.asList(userRole)));
		userRepository.save(user);
	}
}

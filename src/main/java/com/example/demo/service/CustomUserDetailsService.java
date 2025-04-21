package com.example.demo.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.model.UserDto;
import com.example.demo.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {
	
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	
	public CustomUserDetailsService(
		UserRepository userRepository,
		PasswordEncoder passwordEncoder
		)
	{
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}
	
	@Override
	public UserDetails loadUserByUsername(String username)
		throws UsernameNotFoundException
	{
		
		UserDto userData = userRepository.selectUserData(username);
		String roles;
		
		if(username.equals("admin")) roles = "ADMIN";
		else roles = "USER";
		
		return User.withUsername(userData.getUsername())
			.password(userData.getPassword())
			.roles(roles)
			.build();
	}
}

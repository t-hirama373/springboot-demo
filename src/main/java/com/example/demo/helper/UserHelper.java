package com.example.demo.helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.demo.model.RequestUserDto;
import com.example.demo.model.UserDto;

@Component
public class UserHelper {
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	public UserDto convertUserDto(RequestUserDto reqUserData) {
		UserDto userData = new UserDto();
		userData.setUsername(reqUserData.getUsername());
		userData.setPassword(passwordEncoder.encode(reqUserData.getPassword()));
		
		return userData;
	}
}

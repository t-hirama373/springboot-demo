package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.model.UserAuthority;
import com.example.demo.model.UserDto;
import com.example.demo.repository.UserRepository;

@Service
public class UserService implements UserDetailsService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserDto user = userRepository.selectUserData(username);
		if(user == null) {
			throw new UsernameNotFoundException("User not found");
		}
		return new UserAuthority(user);
	}
	
	@Transactional
	public int exsistCheck(String username) {
		return userRepository.selectCountDuplicateUserName(username);
	}
	
	@Transactional
	public void addUserInfo(UserDto userData) {
		userRepository.insertUserData(userData);
	}
	
	//パスワード変更処理確認
	//public void passwordCheck();
	
	//パスワード変更処理実行
	@Transactional
	public void updateUserInfo(String username, String password) {
		userRepository.updateUserData(username, password);
	}
}

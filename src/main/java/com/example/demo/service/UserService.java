package com.example.demo.service;

import java.sql.Timestamp;
import java.time.LocalDateTime;

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
	
	//本棚名の取得
	@Transactional
	public String getShelfName(String ShelfName) {
		return userRepository.selectShelfName(ShelfName);
	}
	
	//パスワード変更処理実行(認証)
	@Transactional
	public void updateUserInfo(String username, String password) {
		userRepository.updateUserData(username, password);
	}
	
	//リセットトークン保存
	@Transactional
	public void updateResetToken(String username, String resetToken) {
		//現在時刻の取得
		Timestamp timestamp =
			Timestamp.valueOf(LocalDateTime.now().plusMinutes(30));
		//リセットトークン＆有効期限を保存
		userRepository.resetTokenIssue(username, resetToken, timestamp);
	}
	
	//リセットトークン取得
	@Transactional
	public String getUsernameByResetToken(String resetToken) {
		return userRepository.selectResetToken(resetToken);
	}
	
	//リセットトークン有効期限取得
	@Transactional
	public Timestamp getResetTokenExpiration(String resetToken) {
		return userRepository.selectResetTokenExpiration(resetToken);
	}
	
	//パスワード変更処理実行(未認証)
	@Transactional
	public void updateUserInfoByToken(String resetToken, String password) {
		userRepository.updateUserDataByToken(resetToken, password);
	}
}

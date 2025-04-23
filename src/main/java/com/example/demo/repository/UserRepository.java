package com.example.demo.repository;

import java.sql.Timestamp;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.example.demo.model.UserDto;

@Repository
@Mapper
public interface UserRepository {
	
	// データベースからユーザ情報取得
	UserDto selectUserData(String username);
	
	// ユーザ名の重複を確認
	int selectCountDuplicateUserName(String username);
	
	// 新規ユーザ登録
	void insertUserData(UserDto userData);
	
	// パスワード更新
	void updateUserData(String username, String password);
	
	//リセットトークン保存
	void resetTokenIssue(
		String username,
		String resetToken,
		Timestamp resetTokenExpiration
	);
	
	//リセットトークン取得
	String selectResetToken(String resetToken);
	
	//リセットトークン有効期限取得
	Timestamp selectResetTokenExpiration(String resetToken);
	
	// パスワード更新
	void updateUserDataByToken(String resetToken, String password);
}
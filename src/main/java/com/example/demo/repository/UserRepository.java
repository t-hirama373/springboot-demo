package com.example.demo.repository;

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
}
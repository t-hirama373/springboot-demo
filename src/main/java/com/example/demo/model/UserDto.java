package com.example.demo.model;

import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {
	
	// ユーザ名
	private String username;
	//パスワード
	private String password;
	// 本棚名
	private String shelfName;
	// リセットトークン
	private String resetToken;
	// リセットトークン発行日時
	private Timestamp resetTokenExiporaion;

}

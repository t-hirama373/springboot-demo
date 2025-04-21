package com.example.demo.model;

import jakarta.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestUserDto {
	
	// ユーザ名
	@NotBlank(message="ユーザ名は必須項目です。")
	private String username;
	
	//パスワード
	@NotBlank(message="パスワードは必須項目です。")
	private String password;

}

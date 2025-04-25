package com.example.demo.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HistoryDto {
	
	//処理項目
	private String process;
	//処理日時
	private String processDate;
	//利用者氏名
	private String username;
	//利用者メール
	private String address;
	//タイトル
	private String bookTitle;
	//ID
	private int bookId;
}

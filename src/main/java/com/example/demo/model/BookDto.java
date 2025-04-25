package com.example.demo.model;

import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookDto {
	
	//ID
	private int id;
	//タイトル
	private String title;
	//著者
	private String author;
	//発行者
	private String publiser; 
	//発行年(null対応=Integer)
	private Integer publiserYear;
	//書籍説明
	private String detail;
	//購入日
	private String purchaseDate;
	//書籍カテゴリ
	private String category;
	//書籍イメージ（登録）
	private byte[] bookImage;
	//書籍イメージ（取得）
	private String bookImageStr;
	//登録日
	private Timestamp createdAt;
	//更新日
	private Timestamp updateAt;
	//貸出状態
	private int status;
	//貸出日
	private Timestamp borrowDate;
	//利用者氏名
	private String username;
	//利用者メール
	private String address;
}

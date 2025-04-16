package com.example.demo.repository;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.example.demo.model.BookDto;

@Repository
@Mapper
public interface BookRepository {
	
	//新規登録
	void insertBookData(BookDto bookData);
	
	//登録更新
	void updateBookData(BookDto bookData);
	
	//書籍IDから書籍情報を取得
	BookDto selectTargetBookData(int id);
	
	//画像
	String findImageBase64ById(int id);
}

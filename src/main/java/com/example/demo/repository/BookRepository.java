package com.example.demo.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.example.demo.model.BookDto;

@Repository
@Mapper
public interface BookRepository {
	
	//書籍一覧を取得する(ID昇順)
	List<BookDto>selectBookDataIdAsc();
	
	//画像一覧を取得する(ID昇順)
	List<String>selectImageBase64byAsc();
	
	//新規登録
	void insertBookData(BookDto bookData);
	
	//貸出処理
	void updateStatusToBorrowed(BookDto bookData);
	
	//返却処理
	void updateStatusToReturned(BookDto bookData);
	
	//登録更新
	void updateBookData(BookDto bookData);
	
	//書籍IDから書籍情報を取得
	BookDto selectTargetBookData(int id);
	
	//書籍IDから画像を取得
	String findImageBase64ById(int id);
}

package com.example.demo.service;

import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.model.BookDto;
import com.example.demo.repository.BookRepository;

@Service
public class BookService {
	
	@Autowired
	private BookRepository bookRepository;
	
	//新規登録
	@Transactional
	public void addBookData(BookDto bookData) {
		//現在時刻の取得
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		//登録日と更新日に現在時刻を代入
		bookData.setCreatedAt(timestamp);
		bookData.setUpdateAt(timestamp);
		//Repository処理実行
		bookRepository.insertBookData(bookData);
	}
	
	@Transactional
	//登録更新
	public void updateBookData(BookDto bookData) {
		//現在時刻の取得
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		//登録日と更新日に現在時刻を代入
		bookData.setUpdateAt(timestamp);
		//Repository処理実行
		bookRepository.updateBookData(bookData);
	}
	
	//書籍ID取得
	@Transactional
	public BookDto getTargetBookData(int id) {
		return bookRepository.selectTargetBookData(id);
	}
	
	//画像取得
	public byte[] getImageById(long id) {
		return bookRepository.findImageById(id);
	}
}

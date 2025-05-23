package com.example.demo.service;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.model.BookDto;
import com.example.demo.repository.BookRepository;

@Service
public class BookService {
	
	@Autowired
	private BookRepository bookRepository;
	
	//一覧表示
	@Transactional
	public List<BookDto> getBookData(Integer status, String sort, String keyword) {
		return bookRepository.selectBookData(status, sort, keyword);
	}
	
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
	
	//貸出処理
	public void updateBookDateBorrowed(BookDto bookData) {
		//現在時刻の取得
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		//貸出日に現在時刻を代入
		bookData.setBorrowDate(timestamp);
		//Repository処理実行
		bookRepository.updateStatusToBorrowed(bookData);
	}
	
	//返却処理
	public void updateBookDateReturned(BookDto bookData) {
		//Repository処理実行
		bookRepository.updateStatusToReturned(bookData);
	}
	
	@Transactional
	//登録更新
	public void updateBookData(BookDto bookData) {
		//現在時刻の取得
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		//更新日に現在時刻を代入
		bookData.setUpdateAt(timestamp);
		//Repository処理実行
		bookRepository.updateBookData(bookData);
	}
	
	//書籍IDから書籍情報を取得
	@Transactional
	public BookDto getTargetBookData(int id) {
		return bookRepository.selectTargetBookData(id);
	}
	
	//書籍IDから画像取得
	public String getImageBase64ById(int id) {
		return bookRepository.findImageBase64ById(id);
	}
	
	//削除
	@Transactional
	public void deleteBookData(int id) {
		bookRepository.deleteBookData(id);
	}
}

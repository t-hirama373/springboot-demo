package com.example.demo.repository;

import java.sql.Timestamp;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.example.demo.model.HistoryDto;

@Repository
@Mapper
public interface HistoryRepository {
	
	//履歴一覧を取得する
	List<HistoryDto>selectHistoryData();
	
	//貸出/返却処理記録
	void insertProcessData(
		String process,
		Timestamp processDate,
		String username,
		String address,
		String bookTitle,
		int bookId);
}

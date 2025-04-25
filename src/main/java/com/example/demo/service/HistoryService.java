package com.example.demo.service;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.model.HistoryDto;
import com.example.demo.repository.HistoryRepository;

@Service
public class HistoryService {
	
	@Autowired
	private HistoryRepository historyRepository;
	
	//一覧表示
	@Transactional
	public List<HistoryDto> getHistoryData() {							
		return historyRepository.selectHistoryData();
	}
	
	//貸出/返却処理記録
	@Transactional
	public void addProcessData(
		String process,
		String username,
		String address,
		String title,
		int id)
	{
		//現在時刻の取得
		Timestamp now = new Timestamp(System.currentTimeMillis());
		//Repository処理実行
		historyRepository.insertProcessData(process,now,username,address,title,id);
	}
}

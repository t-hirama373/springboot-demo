package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.service.BookService;
import com.example.demo.service.UserService;

@Controller
@RequestMapping("/showBookList/{shelfName}")
public class ShowController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private BookService bookService;
	
	//書籍一覧
	@GetMapping
	public String bookList(
		@PathVariable("shelfName") String shelfName,
		@RequestParam(required=false) Integer status,
		@RequestParam(required=false) String sort,
		@RequestParam(required=false) String keyword,
		Model model) {
		//本棚名取得
		String shelf = userService.getShelfName(shelfName);
		if(shelf == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,"指定された書籍一覧が存在しません");
		}
		model.addAttribute("shelf",shelf);
		//書籍情報取得
		model.addAttribute("status", status);
		model.addAttribute("sort", sort);
		model.addAttribute("keyword", keyword);
		model.addAttribute("books", bookService.getBookData(status, sort, keyword));
		//該当件数取得
		model.addAttribute(
			"count",bookService.getBookData(status, sort, keyword).size());
		
		return "showBookList";
	}
}

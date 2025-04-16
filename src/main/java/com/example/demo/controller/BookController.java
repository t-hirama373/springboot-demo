package com.example.demo.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.model.BookDto;
import com.example.demo.service.BookService;

@Controller
@RequestMapping("/bookList")
public class BookController {
	
	@Autowired
	private BookService bookService;
	
	//書籍一覧
	@GetMapping
	public String bookList() {
		return "bookList";
	}
	
	//新規登録へ
	@GetMapping("addBook")
	public String addBook(
		@ModelAttribute("book") BookDto bookDto,
		Model model)
	{
		return "addBook";
	}
	
	//新規登録
	@PostMapping
	public String addBookData(
		@ModelAttribute("book") BookDto request,
		@RequestParam("image") MultipartFile imageFile,
		BindingResult result,
		Model model)
	{
		try {
			if(!imageFile.isEmpty()) {
				request.setBookImage(imageFile.getBytes());
			}
			bookService.addBookData(request);
			return "redirect:/bookList";
		} catch(IOException e) {
			e.printStackTrace();
			model.addAttribute("error", "画像の処理に失敗しました");
			return "addBook";
		}
	}
	
	//登録更新へ
	@GetMapping("{id}/editBook")
	public String editBook(@PathVariable int id, Model model) {
		BookDto result = bookService.getTargetBookData(id);
		BookDto response = result;
		model.addAttribute("book", response);
		return "editBook";
	}
	
	//登録更新
	@PutMapping("{id}")
	public String editBookData(
		@PathVariable int id,
		@ModelAttribute("book") BookDto request,
		Model model)
	{
		bookService.updateBookData(request);
		return "redirect:/bookList";
	}
	
	//画像取得
	@GetMapping("/image/{id}")
	@ResponseBody
	public ResponseEntity<byte[]> getImage(@PathVariable int id){
		byte[] image = bookService.getImageById(id);
		if(image == null) {
			return ResponseEntity.notFound().build();
		}
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.IMAGE_PNG);
		return new ResponseEntity<>(image, HttpStatus.OK);
	}
}

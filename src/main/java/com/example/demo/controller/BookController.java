package com.example.demo.controller;

import java.io.IOException;
import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.model.BookDto;
import com.example.demo.service.BookService;
import com.example.demo.service.HistoryService;
import com.example.demo.service.UserService;

@Controller
@RequestMapping("/bookList")
public class BookController {
	
	@Autowired
	private UserService userService;
	@Autowired
	private BookService bookService;
	@Autowired
	private HistoryService historyService;
	
	//書籍一覧
	@GetMapping
	public String bookList(
		@AuthenticationPrincipal UserDetails user,
		@RequestParam(required=false) Integer status,
		@RequestParam(required=false) String sort,
		@RequestParam(required=false) String keyword,
		Model model) {
		//アカウント名取得
		String username = user.getUsername();
		String shelf = userService.getShelfNameByUsername(username);
		model.addAttribute("shelf",shelf);
		//書籍情報取得
		model.addAttribute("status", status);
		model.addAttribute("sort", sort);
		model.addAttribute("keyword", keyword);
		model.addAttribute("books", bookService.getBookData(status, sort, keyword));
		//該当件数取得
		model.addAttribute(
			"count",bookService.getBookData(status, sort, keyword).size());
		
		return "bookList";
	}
	
	//新規登録へ
	@GetMapping("addBook")
	public String addBook(
		@AuthenticationPrincipal UserDetails user,
		@ModelAttribute("book") BookDto bookDto,
		Model model)
	{
		//アカウント名取得
		String username = user.getUsername();
		model.addAttribute("username",username);
		
		return "addBook";
	}
	
	//貸出履歴へ
	@GetMapping("history")
	public String history(
		@AuthenticationPrincipal UserDetails user,
		@RequestParam(required=false) String process,
		@RequestParam(required=false) String startDate,
		@RequestParam(required=false) String endDate,
		@RequestParam(required=false) String keyword,
		Model model) {
		//アカウント名取得
		String username = user.getUsername();
		model.addAttribute("username",username);
		//履歴情報取得
		model.addAttribute("process", process);
		model.addAttribute("startDate", startDate);
		model.addAttribute("endDate", endDate);
		model.addAttribute("keyword", keyword);
		model.addAttribute("history",
			historyService.getHistoryData(process,startDate,endDate, keyword));
		//該当件数取得
		model.addAttribute("count",
			historyService.getHistoryData(process,startDate,endDate, keyword).size());
		
		return "history";
	}
	
	//貸出管理へ
	@GetMapping("{id}/manageBook")
	public String borrowBook(
		@PathVariable int id,
		@AuthenticationPrincipal UserDetails user,
		Model model)
	{
		//アカウント名取得
		String username = user.getUsername();
		model.addAttribute("username",username);
		//書籍情報取得
		BookDto result = bookService.getTargetBookData(id);
		BookDto response = result;
		model.addAttribute("book", response);
		String base64Image = bookService.getImageBase64ById(id);
		model.addAttribute("base64Image", base64Image);
		if(base64Image != null) {
			System.out.println("Base64="+base64Image.length());
		}
		//現在時刻取得
		Timestamp now = new Timestamp(System.currentTimeMillis());
		model.addAttribute("now",now);
		
		return "manageBook";
	}
	
	//登録更新へ
	@GetMapping("{id}/editBook")
	public String editBook(
		@PathVariable int id,
		@AuthenticationPrincipal UserDetails user,
		Model model) 
	{
		//アカウント名取得
		String username = user.getUsername();
		model.addAttribute("username",username);
		//書籍情報取得
		BookDto result = bookService.getTargetBookData(id);
		BookDto response = result;
		model.addAttribute("book", response);
		String base64Image = bookService.getImageBase64ById(id);
		model.addAttribute("base64Image", base64Image);
		System.out.println("id="+id);
		if(base64Image != null) {
			System.out.println("Base64="+base64Image.length());
		}
		
		return "editBook";
	}
	
	//新規登録
	@PostMapping
	public String addBookData(
		@ModelAttribute("book") BookDto request,
		@RequestParam("image") MultipartFile imageFile,
		@RequestParam("title") String bookTitle,
		BindingResult result,
		RedirectAttributes redirect,
		Model model)
	{
		try {
			if(!imageFile.isEmpty()) {
				request.setBookImage(imageFile.getBytes());
			}
			bookService.addBookData(request);
			redirect.addFlashAttribute("result", "\""+bookTitle+"\"の登録処理を行いました。");
			return "redirect:/bookList";
		} catch(IOException e) {
			e.printStackTrace();
			redirect.addFlashAttribute("error", "画像の処理に失敗しました。");
		}
		return "redirect:addBook";
	}
	
	//貸出・返却処理
	@PutMapping("{id}/manage")
	public String borrowBook(
		@PathVariable int id,
		@ModelAttribute("book") BookDto request,
		@RequestParam("statusBt") String status,
		@RequestParam("username") String username,
		@RequestParam("address") String address,
		@RequestParam("title") String bookTitle,
		@RequestParam("id") int bookId,
		RedirectAttributes redirect,
		Model model)
	{
		String process = "";
		if(status.equals("borrow")) {
			bookService.updateBookDateBorrowed(request);
			process = "貸出";
			redirect.addFlashAttribute("result", "\""+bookTitle+"\"の貸出処理を行いました。");
		}
		if(status.equals("return")) {
			bookService.updateBookDateReturned(request);
			process = "返却";
			redirect.addFlashAttribute("result", "\""+bookTitle+"\"の返却処理を行いました。");
		}
		historyService.addProcessData(process, username, address, bookTitle, bookId);
		return "redirect:/bookList";
	}
	
	//登録更新
	@PutMapping("{id}")
	public String editBookData(
		@PathVariable int id,
		@ModelAttribute("book") BookDto request,
		@RequestParam("image") MultipartFile imageFile,
		@RequestParam("title") String bookTitle,
		RedirectAttributes redirect,
		Model model)
	{
		try {
			if(!imageFile.isEmpty()) {
				request.setBookImage(imageFile.getBytes());
			}
			bookService.updateBookData(request);
			redirect.addFlashAttribute("result", "\""+bookTitle+"\"の更新処理を行いました。");
			return "redirect:/bookList";
		} catch(IOException e) {
			e.printStackTrace();
			redirect.addFlashAttribute("error", "画像の処理に失敗しました。");
		}
		return "redirect:/editBook";
	}
	
	//削除
	@PostMapping("{id}/delete")
	public String delete(
		@PathVariable int id,
		@RequestParam("title") String bookTitle,
		RedirectAttributes redirect)
	{
		System.out.println(bookTitle);
		bookService.deleteBookData(id);
		redirect.addFlashAttribute("important", "\""+bookTitle+"\"の削除処理を行いました。");
		return "redirect:/bookList"; 
	}
}

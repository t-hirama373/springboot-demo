package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.service.UserService;

@Controller
@RequestMapping("/passwordSetting")
public class PasswordSettingController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@GetMapping()
	public String passwordSettingPage()
	{
		return "passwordSetting";
	}
	
	//一覧へ戻る
	@GetMapping("/customerList")
	public String back() {
		return "redirect:/customerList";
	}
	
	//パスワード変更
	@GetMapping("/setting")
	public String passwordSetting(
		@AuthenticationPrincipal UserDetails user,
		@RequestParam(name="newPassword") String newPassword,
		@RequestParam(name="reNewPassword") String reNewPassword,
		RedirectAttributes redirect)
	{	
		//新規パスワードと確認パスワードが不一致
		if(!newPassword.equals(reNewPassword)) {
			redirect.addFlashAttribute("result", "新しいパスワードが一致しません。");
			return "redirect:/passwordSetting";
		}
		
		//未入力のフォームがある
		if(newPassword == "" && reNewPassword == "") {
			redirect.addFlashAttribute("result", "未入力のフォームがあります。");
			return "redirect:/passwordSetting";
		}
		
		//すべて正常に入力後、パスワードが変更される
		userService.updateUserInfo(user.getUsername(),
			passwordEncoder.encode(newPassword));
			redirect.addFlashAttribute("result", "パスワードの変更が完了しました。");
		return "redirect:/passwordSetting";
	}
	
}

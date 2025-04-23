package com.example.demo.controller;

import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
	public String passwordSettingPage(
		@RequestParam(value="token", required = false) String token,
		RedirectAttributes redirectAttribute,
		Authentication auth,
		Model model)
	{
		//ログインからアクセス
		if(auth != null && auth.isAuthenticated()) {
			model.addAttribute("login", "一覧へ戻る");
			return "passwordSetting";
		}
		//トークンからアクセス
		String username = userService.getUsernameByResetToken(token);
		if(username == null) {
			return null;
		}
		Timestamp expiration = userService.getResetTokenExpiration(token);
		if(expiration == null || 
				expiration.before(new Timestamp(System.currentTimeMillis())))
		{
			return null;
		}
		model.addAttribute("token", token);
		return "passwordSetting";
	}
	
	//一覧へ戻る
	@GetMapping("/customerList")
	public String back() {
		return "redirect:/customerList";
	}
	
	//パスワード変更
	@PostMapping("/setting")
	public String passwordSetting(
		@AuthenticationPrincipal UserDetails user,
		@RequestParam(value="token", required = false) String token,
		@RequestParam(name="newPassword") String newPassword,
		@RequestParam(name="reNewPassword") String reNewPassword,
		RedirectAttributes redirect)
	{	
		//新規パスワードと確認パスワードが不一致
		if(!newPassword.equals(reNewPassword)) {
			redirect.addFlashAttribute("error", "新しいパスワードが一致しません。");
			return "redirect:/passwordSetting";
		}
		//未入力のフォームがある
		if(newPassword == "" && reNewPassword == "") {
			redirect.addFlashAttribute("error", "未入力のフォームがあります。");
			return "redirect:/passwordSetting";
		}
		//ログイン時での変更処理
		if(user != null) {
			//パスワード変更
			userService.updateUserInfo(user.getUsername(),
			passwordEncoder.encode(newPassword));
			redirect.addFlashAttribute("success", "パスワードの変更が完了しました。");
			return "redirect:/passwordSetting";
		}
		//トークンアクセスでの変更処理（パスワード変更およびトークンリセット）
		userService.updateUserInfoByToken(token, passwordEncoder.encode(newPassword));
		redirect.addFlashAttribute("success", "パスワードの変更が完了しました。");
		return "redirect:/passwordSetting";
	}
	
}

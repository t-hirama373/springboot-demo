package com.example.demo.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.service.UserService;

@Controller
public class LoginController {
	
	@Autowired
	private UserService userService;
	
	@GetMapping("/login")
	public String login(
		@AuthenticationPrincipal UserDetails user,
		Model model)
	{
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if(auth != null && auth.isAuthenticated() &&
			!(auth instanceof AnonymousAuthenticationToken)) {
			return "redirect:/bookList";
		}
		return "login";
	}
	
	//パスワード再設定URL発行
	@PostMapping("login/sendAddress")
	public String sendAddress(
		HttpServletRequest request,
		@RequestParam(name="sendAddress") String sendAddress,
		RedirectAttributes redirectAttributes)
	{
		try {
			//アカウント有
			UserDetails user = userService.loadUserByUsername(sendAddress);
			System.out.println(user.getUsername());
			//ファイルの保存先を指定
			String userHome = System.getProperty("user.home");
			Path desktopPath = Path.of(userHome, "Desktop","reset_token.txt");
			//リセットトークン発行
			String token = UUID.randomUUID().toString();
			String resetUrl = "http://localhost:8080/passwordSetting?token="+token;
			Files.writeString(desktopPath,resetUrl);
			userService.updateResetToken(user.getUsername(), token);
			redirectAttributes.addFlashAttribute("success", "再設定用のURLを送信しました。");
		} catch(UsernameNotFoundException e) {
			//アカウント無
			redirectAttributes.addFlashAttribute("error", "登録のないアドレスです。");
		} catch(IOException e){
			redirectAttributes.addFlashAttribute("error", "ファイル出力中にエラーが発生しました。");
		}
		return "redirect:/login";
	}
}

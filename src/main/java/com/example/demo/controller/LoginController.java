package com.example.demo.controller;

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
	
	@PostMapping("login/sendAddress")
	public String sendAddress(
		HttpServletRequest request,
		@RequestParam(name="sendAddress") String sendAddress,
		RedirectAttributes redirectAttributes)
	{
		try {
			UserDetails user = userService.loadUserByUsername(sendAddress);
			System.out.println(user.getUsername());
			redirectAttributes.addFlashAttribute("success", "再設定用のURLを送信しました。");
		} catch(UsernameNotFoundException e) {
			redirectAttributes.addFlashAttribute("error", "登録のないアドレスです。");
		}
		return "redirect:/login";
	}
}

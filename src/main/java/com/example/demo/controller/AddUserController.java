package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.helper.UserHelper;
import com.example.demo.model.RequestUserDto;
import com.example.demo.model.UserDto;
import com.example.demo.service.UserService;

@Controller
public class AddUserController {
	
	@Autowired
    private UserService userService;
	
	@Autowired
	private UserHelper userHelper;

    @GetMapping("/addUser")
    public ModelAndView addForm() {
    	ModelAndView output = new ModelAndView();
    	output.addObject("user", new UserDto());
    	output.setViewName("addUser");
        return output;
    }

    @PostMapping("/addUser")
    public String register(@ModelAttribute("user") @Validated RequestUserDto userData, BindingResult result, Model model) {
        // バリデーションチェックの結果でエラーがあった場合
    	if(result.hasErrors()) {
        	return "addUser";
        }
        // 入力されたユーザー名の重複チェック
    	int existCount = userService.exsistCheck(userData.getUsername());
        // すでに使用されている場合は新規登録画面に戻る
        if(existCount != 0){
        	model.addAttribute("errorMessage", "既に登録されているユーザー名です。");
            return "addUser";
        }
        // 使用されていない場合はデータベースに追加
        userService.addUserInfo(userHelper.convertUserDto(userData));
        return "login";
    }
    

}
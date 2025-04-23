package com.example.demo.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.example.demo.service.CustomUserDetailsService;

@Configuration
@EnableMethodSecurity
public class WebSecurityConfig implements WebMvcConfigurer {
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	protected SecurityFilterChain securityFilterChain(
		HttpSecurity http,
		CustomUserDetailsService customUserDetailsService
		) throws Exception
	{
		http
			.authorizeHttpRequests(request -> request
				// 認証（ログイン）が不要なアクセス
				.requestMatchers(
					"/login",
					"/login/sendAddress",
					"/login/send",
					"/addUser",
					"/css/**",
					"/js/**",
					"/error/**",
					"/passwordSetting/**"
				).permitAll()				
				// 認証（ログイン）が必要なアクセス
				.anyRequest().authenticated()
	        )
			//adminまたはuserを照会
			.userDetailsService(customUserDetailsService)
			//
			.formLogin(login -> login
				.loginPage("/login")
				.loginProcessingUrl("/doLogin")
				.defaultSuccessUrl("/bookList")
				.permitAll()
	        )
			.logout(logout -> logout
				.logoutUrl("/logout")
				//ログアウト後にログイン画面へ
				.logoutSuccessUrl("/login?logout")
				//セッションを破棄
				.invalidateHttpSession(true)
				//クッキー削除で再ログインを強制
				.deleteCookies("JSESSIONID")
				.permitAll()
	        )
			//キャッシュを無効
			.headers(headers -> headers
				.cacheControl(cache -> cache.disable())
			);
		
		return http.build();
	}
	
	@Override
	//割り込み処理
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new NoCacheInterceptor());
	}
	
	//ログアウト後のブラウザバックを制限
	private static class NoCacheInterceptor implements HandlerInterceptor {
		@Override
		public void postHandle(
			
			HttpServletRequest request,
			HttpServletResponse response,
			Object handler,
			ModelAndView modelAndView)
		{
			//セットヘッダー（ｸﾗｲｱﾝﾄにﾚｽﾎﾟﾝｽを返す際、ヘッダーを新規に追加したり上書き）
			//***.setHeader(String name, String value);
			response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
			response.setHeader("Pregma", "no-cache");
			response.setHeader("Expires", "0");
		}
	}

}
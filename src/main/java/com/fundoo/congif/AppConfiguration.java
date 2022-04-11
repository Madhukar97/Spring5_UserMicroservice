package com.fundoo.congif;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;

import com.fundoo.utils.JwtToken;
import com.fundoo.utils.AppUtils;

@Configuration
public class AppConfiguration {

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

	@Bean
	public AppUtils appUtils() {
		return new AppUtils();
	}

	// Password Encoder configuration bean
	@Bean
	public PasswordEncoder passwordencoder() {
		return new BCryptPasswordEncoder();
	}

	// JwtToken configuration bean
	@Bean
	public JwtToken jwtToken() {
		return new JwtToken();
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
	
	@Bean
    public JavaMailSender javaMailSender() {
        return new JavaMailSenderImpl();
    }
	
}

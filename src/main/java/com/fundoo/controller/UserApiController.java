package com.fundoo.controller;

import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fundoo.dto.LoginDto;
import com.fundoo.dto.UserDto;
import com.fundoo.service.EmailService;
import com.fundoo.service.IService;
import com.fundoo.service.UserService;
import com.fundoo.utils.Response;

import reactor.core.publisher.Mono;

@RestController
@CrossOrigin(origins="http://localhost:3000")
@RequestMapping("/usermicroservice")
public class UserApiController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private IService iService;
	
	@PostMapping("/register")
	public Mono<Response> registerUser(@RequestBody UserDto userDto){
		return userService.registerUser(userDto);
	}
	

	@PutMapping("/update/{id}")
	public Mono<Response> updateUser(@PathVariable String id, @RequestBody UserDto user) {
		Mono<Response> msg = iService.updateUser(user, id);
		return msg;
	}

	@DeleteMapping("/delete/{id}")
	public Mono<Response> deleteUser(@PathVariable String id){
		Mono<Response> msg = iService.deleteUser(id);
		return msg;
	}

	@PostMapping("/user_login")
	public Mono<Response> validateUserLogin(@RequestBody LoginDto loginDto){
		Mono<Response> msg = iService.validateUserLogin(loginDto);
		return msg;
	}

	@PutMapping("/resetpassword/{password}")
	public Mono<Response> resetPassword (@PathVariable String password, @RequestHeader String token) {
		Mono<Response> msg = iService.resetPassword(password, token);
		return msg;
	}

	@GetMapping("/verify")
	public Mono<String> verifyAccount(@RequestParam String token) {
		Mono<String> msg = iService.verifyUser(token);
		return msg;
	}


	@PostMapping("/forgotPass/{email}")
	public Mono<String> forgotPass(@PathVariable String email) throws UnsupportedEncodingException, MessagingException {
		String siteUrl = "http://localhost:3000/resetpass";
		emailService.sendForgotPassEmail(email, siteUrl);
		Mono<String> msg = iService.forgotPassword(email);
		return msg;
	}

	@GetMapping("/getuserid/{token}")
	public Mono<String> getUserId(@PathVariable String token){
		Mono<String> id = iService.getUserId(token);
		return id;
	}
	
	@GetMapping("/getuseremail/{id}")
	public Mono<String> getUserEmail(@PathVariable String id){
		Mono<String> email = iService.getUserEmail(id);
		return email;
	}

}

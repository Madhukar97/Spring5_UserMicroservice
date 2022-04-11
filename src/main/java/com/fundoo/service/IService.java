package com.fundoo.service;

import com.fundoo.dto.LoginDto;
import com.fundoo.dto.UserDto;
import com.fundoo.utils.Response;

import reactor.core.publisher.Mono;

public interface IService {

	Mono<Response> registerUser(UserDto userDto);

	Mono<Response> getAllUsers();

	Mono<String> verifyUser(String token);

	Mono<Response> updateUser(UserDto userDto, String id);

	Mono<Response> deleteUser(String id);

	Mono<Response> validateUserLogin(LoginDto loginDto);

	Mono<Response> resetPassword(String password, String token);

	Mono<String> forgotPassword(String email);

	Mono<String> getUserId(String token);

	Mono<String> getUserEmail(String id);

	


}

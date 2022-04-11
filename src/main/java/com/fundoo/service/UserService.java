package com.fundoo.service;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;

import javax.mail.MessagingException;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.fundoo.dto.LoginDto;
import com.fundoo.dto.UserDto;
import com.fundoo.model.User;
import com.fundoo.repo.UserRepository;
import com.fundoo.utils.JwtToken;
import com.fundoo.utils.Response;

import reactor.core.publisher.Mono;

@Service
public class UserService implements IService{

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private JwtToken jwtToken;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private EmailService emailService;

	@Override
	public Mono<Response> registerUser(UserDto userDto) {

		return userRepository.findByEmail(userDto.getEmail()).map(user -> {

			return new Response(406, "Email already exist", null);
		}).switchIfEmpty(Mono.defer(() -> {
			User newUser = modelMapper.map(userDto, User.class);
			return userRepository.save(newUser).map(entity -> {
				try {
					emailService.sendVerificationEmail("http://localhost:8083/usermicroservice/", entity.getId(), entity.getEmail());
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (MessagingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return new Response(200, "User Registration succesful", entity);
			});
		}));

	}

	@Override
	public Mono<Response> getAllUsers() {

		return userRepository.findAll().collectList().map(userList -> {
			return new Response(200, "List of users", userList);
		});
	}
	
	@Override
	public Mono<String> verifyUser(String token){
		String email = jwtToken.decodeTokenUsername(token);
		return userRepository.findByEmail(email).flatMap(user -> {
			user.setIsVerified(true);
			return userRepository.save(user).map(entity -> {
				return "Email Verification Successful...Now you can login to your account.";
			});
		});
		
	}
	
	@Override
	public Mono<Response> updateUser(UserDto userDto, String id){
			return userRepository.findById(id).flatMap(user -> {
			user.setEmail(userDto.getEmail());
			user.setFirstName(userDto.getFirstName());
			user.setLastName(userDto.getLastName());
			user.setPassword(userDto.getPassword());
			return userRepository.save(user).map(updatedUser -> {
				return new Response(200, "User updated successfully", updatedUser);
			});
		});
	}
	
	@Override
	public Mono<Response> deleteUser(String id){
		return userRepository.deleteById(id).map(res -> {
			return new Response(200, "User deleted with id: " + id, null);
		});
	}

	@Override
	public Mono<Response> validateUserLogin(LoginDto loginDto){
		return userRepository.findByEmail(loginDto.getEmail()).map(validUser -> {
			if(passwordEncoder.matches(loginDto.getPassword(), validUser.getPassword()) && validUser.getIsVerified()) {
				String token = jwtToken.createToken(validUser.getEmail(), validUser.getId());
				return new Response(200, "Login successful!", token);
			}
			else return new Response(200, "Invalid Password", null);
		}).switchIfEmpty(Mono.just(new Response(404, "Invalid email...user not found!", null)));
	}
	
	@Override
	public Mono<Response> resetPassword(String password, String token){
		String email = jwtToken.decodeTokenUsername(token);
		return userRepository.findByEmail(email).flatMap(user -> {
			user.setPassword(passwordEncoder.encode(password));
			user.setRegisterDate(LocalDateTime.now());
			return userRepository.save(user).map(entity -> {
				return new Response(200, "Password reset successful....!",entity);
			}).switchIfEmpty(Mono.just(new Response(200, "Invalid credentials....User not found!", null)));
		});
	}

	@Override
	public Mono<String> forgotPassword(String email) {
		return userRepository.findByEmail(email).map(user -> {
			String token = jwtToken.createToken(email, user.getId());
			return token;
		});
	}
	
	@Override
	public Mono<String> getUserId(String token) {
		return Mono.just(jwtToken.decodeTokenUserId(token));
	}

	@Override
	public Mono<String> getUserEmail(String id) {
		return userRepository.findById(id).map(user -> {
			return user.getEmail();
		});
	}
	
	

}






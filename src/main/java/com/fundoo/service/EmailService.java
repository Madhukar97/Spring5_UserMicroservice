package com.fundoo.service;

import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.fundoo.repo.UserRepository;
import com.fundoo.utils.JwtToken;

import reactor.core.publisher.Mono;

@Service
public class EmailService {

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private JwtToken jwtToken;

	@Autowired
	private JavaMailSender mailSender;

	public void sendVerificationEmail( String siteUrl, String userId, String email) throws UnsupportedEncodingException, MessagingException{
		
//		User validUser = userRepo.findByEmail(userDto.getEmail());
//		String token = jwtToken.createToken(validUser.getEmail(), validUser.getId());
		String token = jwtToken.createToken(email, userId);
		String verifyUrl = siteUrl + "verify?token=" + token;
		String subject = "Please verify your registration";
		String senderName = "Fundoo Team";
		String mailContent = "<p>Please click link below to verify your registration email</p>";
		mailContent += "<a href = " + verifyUrl +  ">VERIFY</a>";

		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);

		helper.setFrom(System.getenv("fundooemail"), senderName);
		helper.setTo(email);
		helper.setSubject(subject);
		helper.setText(mailContent, true);

		mailSender.send(message);
	}

	public Mono<String> sendForgotPassEmail(String email, String siteUrl) throws UnsupportedEncodingException, MessagingException{
//		User validUser = userRepo.findByEmail(email);
		
		return userRepo.findByEmail(email).map(user -> {
			String token = jwtToken.createToken(user.getEmail(), user.getId());
			String verifyUrl = siteUrl + "/" + token;
			String subject = "Please click on the link to reset your password";
			String senderName = "Fundoo Team";
			String mailContent = "<p>Please click link below to reset your password</p>";
			mailContent += "<a href = " + verifyUrl +  ">Reset Password</a>";

			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message);

			try {
				helper.setFrom("${spring.mail.username}", senderName);
				helper.setTo(email);
				helper.setSubject(subject);
				helper.setText(mailContent, true);

			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (MessagingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			mailSender.send(message);
			return "Check your mail to reset your password";
		});
	}

	

}

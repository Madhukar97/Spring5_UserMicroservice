package com.fundoo.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Document(collection = "User")
public class User {

	@Id
	private String id;

	
	private String firstName;

	
	private String lastName;

	
	private String email;

	
	private String password;

	
	private Boolean isVerified = false;

	
	private LocalDateTime registerDate = LocalDateTime.now();

}

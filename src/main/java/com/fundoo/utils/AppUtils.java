package com.fundoo.utils;

import org.springframework.beans.BeanUtils;

import com.fundoo.dto.UserDto;
import com.fundoo.model.User;

public class AppUtils {

	
	public static User dtoToEntity(UserDto userDto) {
		User user = new User();
		BeanUtils.copyProperties(userDto, user);
		return user;
	}
}

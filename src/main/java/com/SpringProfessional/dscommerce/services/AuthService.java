package com.SpringProfessional.dscommerce.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.SpringProfessional.dscommerce.entities.User;
import com.SpringProfessional.dscommerce.services.exceptions.ForbbidenException;

@Service
public class AuthService {
	
	@Autowired
	private UserService userService;
	
	public void validateSelfOrAdmin(Long userId) {
		User me = userService.authenticated();
		if(!me.hasRole("ROLE_ADMIN") && !me.getId().equals(userId)) {
			throw new ForbbidenException("Access Denied!");
		}
		
	}

}

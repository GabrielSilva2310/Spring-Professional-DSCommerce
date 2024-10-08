package com.SpringProfessional.dscommerce.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.SpringProfessional.dscommerce.UserFactory;
import com.SpringProfessional.dscommerce.entities.User;
import com.SpringProfessional.dscommerce.services.exceptions.ForbbidenException;

@ExtendWith(SpringExtension.class)
public class AuthServiceTests {
	
	@InjectMocks
	private AuthService service;
	
	@Mock
	private UserService userService;
	
	private User admin, selfClient, otherClient; 
	
	
	
	@BeforeEach
	void set() throws Exception {
		admin=UserFactory.createAdminUser();
		selfClient=UserFactory.createCustomClientUser(2L, "ana@gmail.com");
		otherClient=UserFactory.createCustomClientUser(3L, "maria@gmail.com");
	}
	
	@Test
	public void validateSelfOrAdminDoNothingWhenAdminLogged() {
		Mockito.when(userService.authenticated()).thenReturn(admin);
		
		Long userId=admin.getId();
		
		Assertions.assertDoesNotThrow(()->{
			service.validateSelfOrAdmin(userId);
		});
		
		
	}
	
	@Test
	public void validateSelfOrAdminDoNothingWhenSelfClientLogged() {
		Mockito.when(userService.authenticated()).thenReturn(selfClient);
		
		Long userId=selfClient.getId();
		
		Assertions.assertDoesNotThrow(()->{
			service.validateSelfOrAdmin(userId);
		});
		
		
	}
	
	@Test
	public void validateSelfOrAdminShouldThrowForbbidenExceptionWhenOtherClientLogged() {
		Mockito.when(userService.authenticated()).thenReturn(selfClient);
		
		Long userId=otherClient.getId();
		
		Assertions.assertThrows(ForbbidenException.class, ()->{
			service.validateSelfOrAdmin(userId);
		});
		
		
	}
	
	
	
}

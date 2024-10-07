package com.SpringProfessional.dscommerce.services;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.SpringProfessional.dscommerce.UserDetailsFactory;
import com.SpringProfessional.dscommerce.UserFactory;
import com.SpringProfessional.dscommerce.entities.User;
import com.SpringProfessional.dscommerce.projections.UserDetailsProjection;
import com.SpringProfessional.dscommerce.repositories.UserRepository;
import com.SpringProfessional.dscommerce.util.CustomUserUtil;

@ExtendWith(SpringExtension.class)
public class UserServiceTests {
	
	@InjectMocks
	private UserService service;
	
	@Mock	
	private UserRepository repository;
	
	@Mock
	private CustomUserUtil customUserUtil;
	
	private String existingUsername, notExistingUsername;
	
	private List<UserDetailsProjection> userDetails;
	private User user;
	
	@BeforeEach
	void set() throws Exception {
		
		existingUsername="maria@gmail.com";
		notExistingUsername="user@gmail.com";
		user=UserFactory.createCustomClientUser(1L, existingUsername);
		userDetails=UserDetailsFactory.createCustomClientUser(existingUsername);
		
		Mockito.when(repository.searchUserAndRolesByEmail(existingUsername)).thenReturn(userDetails);
		Mockito.when(repository.searchUserAndRolesByEmail(notExistingUsername)).thenReturn(new ArrayList<>());
		Mockito.when(repository.findByEmail(existingUsername)).thenReturn(Optional.of(user));
		Mockito.when(repository.findByEmail(notExistingUsername)).thenReturn(Optional.empty());

	}
	
	@Test
	public void loadUserByUsernameShouldReturnUserDetailsWhenUserExists() {
		UserDetails result=service.loadUserByUsername(existingUsername);
		
		Assertions.assertNotNull(result);
		Assertions.assertEquals(result.getUsername(), existingUsername);
	}
	
	@Test
	public void loadUserByUsernameShouldThrowUsernameNotFoundExceptionWhenDoesNotexistUser() {
		Assertions.assertThrows(UsernameNotFoundException.class, ()->{
			service.loadUserByUsername(notExistingUsername);
		});
		
	}
	
	@Test
	public void authenticatedShouldReturnUserWhenUserExists() {
		
		Mockito.when(customUserUtil.getLoggedUsername()).thenReturn(existingUsername);
		
		User result=service.authenticated();
		
		Assertions.assertNotNull(result);
		Assertions.assertEquals(result.getUsername(), existingUsername);
	
	}
	
	@Test
	public void authenticatedShouldThrowUsernameNotFoundExceptionWhenDoesNotExistUser() {
		
		Mockito.doThrow(ClassCastException.class).when(customUserUtil).getLoggedUsername();
		
		Assertions.assertThrows(UsernameNotFoundException.class, ()->{
			service.authenticated();
		});
	
	}
	
	

}

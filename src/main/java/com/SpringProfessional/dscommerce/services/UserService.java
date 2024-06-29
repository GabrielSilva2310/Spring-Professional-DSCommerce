package com.SpringProfessional.dscommerce.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.SpringProfessional.dscommerce.dto.UserDTO;
import com.SpringProfessional.dscommerce.entities.User;
import com.SpringProfessional.dscommerce.projections.UserDetailsProjection;
import com.SpringProfessional.dscommerce.repositories.UserRepository;

@Service
public class UserService implements UserDetailsService {

	@Autowired
	private UserRepository repository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		List<UserDetailsProjection> result=repository.searchUserAndRolesByEmail(username);
		
		if(result.size() == 0) {
			throw new UsernameNotFoundException("User not found!");
		}
		
		return new User(result);
		
	}
	
	protected User authenticated() {
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			Jwt jwtPrincipal = (Jwt) authentication.getPrincipal();
			String username = jwtPrincipal.getClaim("username");
			return repository.findByEmail(username).get();
		}
		catch(Exception e) {
			throw new UsernameNotFoundException("User not found!");
		}
	}
	
	@Transactional(readOnly = true)
	public UserDTO getMe() {
		User user=authenticated();
		return new UserDTO(user);
	}

}

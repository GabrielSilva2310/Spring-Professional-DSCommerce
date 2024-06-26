package com.SpringProfessional.dscommerce.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

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

}

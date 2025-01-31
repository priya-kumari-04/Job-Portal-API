package com.cherrywine.jobportal.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.cherrywine.jobportal.entity.Users;
import com.cherrywine.jobportal.repository.UsersRepository;
import com.cherrywine.jobportal.util.CustomUserDetails;

@Service
public class CustomUserDetailsService implements UserDetailsService {
	
	private final UsersRepository usersRepository;
	
	@Autowired
	public CustomUserDetailsService(UsersRepository usersRepository) {
		this.usersRepository = usersRepository;
	}


	// Tell SS how to retrieve the users from the database
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Users user = usersRepository.findByEmail(username).orElseThrow(()-> new UsernameNotFoundException("Could not found user"));
		return new CustomUserDetails(user);
	}

}

package com.cherrywine.jobportal.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.cherrywine.jobportal.entity.RecruiterProfile;
import com.cherrywine.jobportal.entity.Users;
import com.cherrywine.jobportal.repository.RecruiterProfileRepository;
import com.cherrywine.jobportal.repository.UsersRepository;

@Service
public class RecruiterProfileService {

	private final RecruiterProfileRepository recruiterRepository;
	private final UsersRepository usersRepository;

	@Autowired
	public RecruiterProfileService(RecruiterProfileRepository recruiterProfileRepository,
			UsersRepository usersRepository) {
		this.recruiterRepository = recruiterProfileRepository;
		this.usersRepository = usersRepository;
	}

	public Optional<RecruiterProfile> getOne(Integer id) {
		return recruiterRepository.findById(id);
	}

	public RecruiterProfile addNew(RecruiterProfile recruiterProfile) {
		return recruiterRepository.save(recruiterProfile);
	}

	public RecruiterProfile getCurrentRecruiterProfile() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!(authentication instanceof AnonymousAuthenticationToken)) {
			String currentUsername = authentication.getName();
			Users users = usersRepository.findByEmail(currentUsername)
					.orElseThrow(() -> new UsernameNotFoundException("User not found"));
			Optional<RecruiterProfile> recruiterProfile = getOne(users.getUserId());
			return recruiterProfile.orElse(null);
		} else return null;
	}

}

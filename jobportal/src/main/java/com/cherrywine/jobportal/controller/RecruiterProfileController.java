package com.cherrywine.jobportal.controller;

import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.cherrywine.jobportal.entity.RecruiterProfile;
import com.cherrywine.jobportal.entity.Users;
import com.cherrywine.jobportal.repository.UsersRepository;
import com.cherrywine.jobportal.services.RecruiterProfileService;
import com.cherrywine.jobportal.util.FileUploadUtil;

@Controller
@RequestMapping("recruiter-profile")
public class RecruiterProfileController {

	private final UsersRepository usersRepository;
	private final RecruiterProfileService recruiterProfileService;

	@Autowired
	public RecruiterProfileController(UsersRepository usersRepository,
			RecruiterProfileService recruiterProfileService) {
		this.usersRepository = usersRepository;
		this.recruiterProfileService = recruiterProfileService;
	}

	@GetMapping("/")
	public String recruiterProfile(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!(authentication instanceof AnonymousAuthenticationToken)) {
			String currentUsername = authentication.getName();
			Users users = usersRepository.findByEmail(currentUsername)
					.orElseThrow(() -> new UsernameNotFoundException("Could not found user"));
			Optional<RecruiterProfile> recruiterProfile = recruiterProfileService.getOne(users.getUserId());
			if (!recruiterProfile.isEmpty())
				model.addAttribute("profile", recruiterProfile.get());
		}

		return "recruiter_profile";

	}

	// Creates a new recruiter profile (in memory) based on form data
	@PostMapping("/addNew")
	public String addNew(RecruiterProfile recruiterProfile, @RequestParam("image") MultipartFile multipartFile,
			Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!(authentication instanceof AnonymousAuthenticationToken)) {
			String currentUsername = authentication.getName();
			Users users = usersRepository.findByEmail(currentUsername)
					.orElseThrow(() -> new UsernameNotFoundException("Could not found user"));
			// Associate recruiter profile with existing user account
			recruiterProfile.setUserId(users);
			recruiterProfile.setUserAccountId(users.getUserId());
		}
		model.addAttribute("profile", recruiterProfile);
		String fileName = "";
		if (!multipartFile.getOriginalFilename().equals("")) {
			fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
			recruiterProfile.setProfilePhoto(fileName);
			// This sets image name in recruiter profile
		}
		RecruiterProfile savedUser = recruiterProfileService.addNew(recruiterProfile); // save recruiter profile to db

		// Read profile image from request - multipartfile and save image on the server
		// in directory: photos/recruiter
		String uploadDir = "photos/recruiter/" + savedUser.getUserAccountId();
		try {
			FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return "redirect:/dashboard/";
	}
}

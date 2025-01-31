package com.cherrywine.jobportal.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cherrywine.jobportal.entity.JobPostActivity;
import com.cherrywine.jobportal.entity.JobSeekerProfile;
import com.cherrywine.jobportal.entity.JobSeekerSave;
import com.cherrywine.jobportal.repository.JobSeekerSaveRepository;

@Service
public class JobSeekerSaveService {

	private final JobSeekerSaveRepository jobSeekerSaveRepository;
	
	public JobSeekerSaveService(JobSeekerSaveRepository jobSeekerSaveRepository) {
		this.jobSeekerSaveRepository = jobSeekerSaveRepository;
	}

	public List<JobSeekerSave> getCandidatesJob(JobSeekerProfile userAccountId){
		return jobSeekerSaveRepository.findByUserId(userAccountId);
	}
	
	public List<JobSeekerSave> getJobCandidates(JobPostActivity job){
		return jobSeekerSaveRepository.findByJob(job);
	}

	public void addNew(JobSeekerSave jobSeekerSave) {
		jobSeekerSaveRepository.save(jobSeekerSave);
	}
	
}

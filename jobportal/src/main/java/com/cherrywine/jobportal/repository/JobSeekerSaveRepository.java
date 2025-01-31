package com.cherrywine.jobportal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cherrywine.jobportal.entity.JobPostActivity;
import com.cherrywine.jobportal.entity.JobSeekerProfile;
import com.cherrywine.jobportal.entity.JobSeekerSave;

@Repository
public interface JobSeekerSaveRepository extends JpaRepository<JobSeekerSave, Integer>{

	public List<JobSeekerSave> findByUserId(JobSeekerProfile userAccountId);
	
	List<JobSeekerSave> findByJob(JobPostActivity job);
}

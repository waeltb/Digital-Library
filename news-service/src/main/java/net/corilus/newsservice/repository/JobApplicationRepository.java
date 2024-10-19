package net.corilus.newsservice.repository;

import net.corilus.newsservice.entity.JobApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobApplicationRepository extends JpaRepository<JobApplication,Long> {
}

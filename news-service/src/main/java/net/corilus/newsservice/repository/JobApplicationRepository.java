package net.corilus.newsservice.repository;


import net.corilus.newsservice.entity.JobApplication;
import net.corilus.newsservice.enums.ApplicationStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
@Repository
public interface JobApplicationRepository extends JpaRepository<JobApplication,Long> {
    Page<JobApplication> findByJobOffer_OfferId(int offerId, Pageable pageable);
    Page<JobApplication>findByUserId(int userId, Pageable pageable);
    Page<JobApplication> findByUserIdAndStatus(int userId, ApplicationStatus status, Pageable pageable);

}

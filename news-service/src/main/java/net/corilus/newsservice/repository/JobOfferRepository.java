package net.corilus.newsservice.repository;

import net.corilus.newsservice.entity.JobOffer;
import net.corilus.newsservice.enums.Categories;
import net.corilus.newsservice.enums.ExperienceLevel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobOfferRepository extends JpaRepository<JobOffer,Long> {
    Page<JobOffer> findByCategories(Categories category, Pageable pageable);
    Page<JobOffer>findByLocation(String location, Pageable pageable);
    Page<JobOffer>findBySalaryRangeAfter(String salary, Pageable pageable);
    Page<JobOffer>findByCompany(String company, Pageable pageable);
    Page<JobOffer>findByExperienceLevel(ExperienceLevel experienceLevel, Pageable pageable);
    Page<JobOffer>findAll(Pageable pageable);
}

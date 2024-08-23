package net.corilus.newsservice.entity;

import jakarta.persistence.*;
import lombok.*;
import net.corilus.newsservice.enums.Categories;
import net.corilus.newsservice.enums.ExperienceLevel;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class JobOffer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  int offerId;
    private  int userId;

    private  String title;

    private  String missions;

    private  String skills;
    @Enumerated(EnumType.STRING)
        private Categories categories;
    @Enumerated(EnumType.STRING)
    private ExperienceLevel experienceLevel;
    private String company;
    private String location;
    private String containername;
    private String salaryRange;
    private String logo;
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date endDateForSending ;
    @OneToMany(mappedBy = "jobOffer")
    private List<JobApplication > jobApplications;
}

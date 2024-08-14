package net.corilus.newsservice.entity;

import jakarta.persistence.*;
import lombok.*;
import net.corilus.newsservice.enums.ExperienceLevel;

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
    private  String context;
    private  String missions;
    private  String profile;
    private  String skills;
    @Enumerated(EnumType.STRING)
    private ExperienceLevel experienceLevel;
    private String company;
    private String location;
    private String salaryRange;
    private String logo;
    @OneToMany(mappedBy = "jobOffer")
    private List<JobApplication > jobApplications;
}

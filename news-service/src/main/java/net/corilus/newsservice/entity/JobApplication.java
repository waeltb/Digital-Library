package net.corilus.newsservice.entity;

import jakarta.persistence.*;
import lombok.*;
import net.corilus.newsservice.enums.ApplicationStatus;

import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class JobApplication  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  int applicationId;
    private  int userId;
    private Date applicationDate;
    private String cv;
    private String coverLetter;
    @Enumerated(EnumType.STRING)
    private ApplicationStatus status;
    @ManyToOne
    private JobOffer jobOffer ;


}

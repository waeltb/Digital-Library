package net.corilus.courseservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  int id;
    private int instructorId ;
    private int expertId ;
    private  String title;
    private  float price ;
    private  String description;
    private  Date creationdate;
    private  Date modificationdate;
    private  String reasonForRefusal;
    private  String descriptiveVideo ;
    private String image;
    private  String speciality ;
    @Enumerated(EnumType.STRING)
    private   Status status ;
    @Enumerated(EnumType.STRING)
    private  Level level;
    @OneToMany(mappedBy = "course")
    private  List<Lesson>lessons ;

}

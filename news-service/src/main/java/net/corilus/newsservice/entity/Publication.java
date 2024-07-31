package net.corilus.newsservice.entity;

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
public class Publication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  int id;
    private int authorId ;
    private  String title;
    private  String description;
    private Date creationdate;
    private String image;
    private  String speciality ;
    private String containername;
    @OneToMany(mappedBy = "publication")
    private List<Comment> comments;
}

package net.corilus.courseservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  int id;
    private  String title;
    private String containername;
    @ManyToOne
    private  Course course;
    @OneToMany(mappedBy = "lesson",cascade = CascadeType.ALL,orphanRemoval = true)
    private  List<Slide> slides  ;


}

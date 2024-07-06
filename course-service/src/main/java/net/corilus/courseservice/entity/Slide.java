package net.corilus.courseservice.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Slide {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  int id;
    private  String title;
    @Column(name = "`order`")
    private  int order;
    private  String pathvideo;
    @ManyToOne
    private  Lesson lesson;


}

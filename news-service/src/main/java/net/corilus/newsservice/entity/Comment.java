package net.corilus.newsservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
        private  int id;
    private int authorId ;
    private  String description;
    private Date creationdate;
    @ManyToOne
    private Publication publication;

}

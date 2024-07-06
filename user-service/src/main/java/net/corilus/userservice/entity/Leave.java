package net.corilus.userservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;


@Table(name = "`leave`")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Leave {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id ;

    private Date startDate ;
    private Date endDate ;


    private Boolean accepted ;

    @Enumerated(EnumType.STRING)
    private Typesofleave typesofleave ;


    private String imagepath ;

    @ManyToOne
    private User user ;




}

package net.corilus.newsservice.modal;

import lombok.*;

import net.corilus.newsservice.enums.Typesofleave;

import java.util.Date;


@Data
public class Leave {

    private int id ;

    private Date startDate ;
    private Date endDate ;


    private Boolean accepted ;


    private Typesofleave typesofleave ;


    private String imagepath ;


    private User user ;




}


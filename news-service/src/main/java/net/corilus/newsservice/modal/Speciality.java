package net.corilus.newsservice.modal;

import lombok.*;

import java.util.List;

@Data
public class Speciality {
    private int id ;
    private String name;
    private List<User> users ;
}
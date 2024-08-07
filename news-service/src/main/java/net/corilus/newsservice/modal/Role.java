package net.corilus.newsservice.modal;

import lombok.Data;

import java.util.List;

@Data
public class Role {

    private int id ;
    private String name;

    private List<User> users;
}

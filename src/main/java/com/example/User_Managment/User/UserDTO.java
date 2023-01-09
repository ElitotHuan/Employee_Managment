package com.example.User_Managment.User;

import lombok.Data;

@Data
public class UserDTO {

    private Long id;

    private String name;

    private int age;

    private String position;

    private double salary;

    public UserDTO(Long id, String name, int age, String position, double salary) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.position = position;
        this.salary = salary;
    }


}


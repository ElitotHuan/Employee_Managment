package com.example.Employee_Managment.models;

import com.sun.istack.NotNull;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@Entity
@Validated
@Table(name = "User_db")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", columnDefinition = "bigserial")
    private Long userId;

    @Column(columnDefinition = "TEXT")
    @NotEmpty
    private String name;

    @Column(columnDefinition = "TEXT")
    @NotEmpty
    private String username;

    @Column(columnDefinition = "TEXT")
    @NotEmpty
    private String password;

    @NotNull
    @Max(55)
    @Min(20)
    private int age;

    @Column(columnDefinition = "TEXT")
    @NotEmpty
    private String position;

    @NotNull
    @Max(1000000)
    @Min(1000)
    private double salary;

    @OneToOne(mappedBy = "user" , cascade = CascadeType.ALL)
    private Login login;

    @OneToOne(mappedBy = "user" , cascade = CascadeType.ALL)
    private Token token;


    public User() {
    }

    @Data
    public static class UserID {
        private Long id;
    }

}

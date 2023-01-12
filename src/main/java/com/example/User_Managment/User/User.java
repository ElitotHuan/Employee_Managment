package com.example.User_Managment.User;

import com.example.User_Managment.Authenticate.Token;
import com.example.User_Managment.Login.Login;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.*;

@Data
@Entity
@Table(name = "User_db")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", columnDefinition = "bigserial")
    private Long userId;

    @Column(columnDefinition = "TEXT")
    @NotEmpty(message = " is required")
    private String name;

    @Column(columnDefinition = "TEXT")
    @NotEmpty(message = " is required")
    private String username;

    @Column(columnDefinition = "TEXT")
    @NotEmpty(message = " is required")
    private String password;

    @NotNull
    @Max(55)
    @Min(20)
    private int age;

    @Column(columnDefinition = "TEXT")
    @NotEmpty(message = " is required")
    private String position;

    @NotNull
    @Max(1000000)
    @Min(1000)
    private double salary;

    @OneToOne(mappedBy = "user" , cascade = CascadeType.ALL)
    private Login login;

    @OneToOne(mappedBy = "user" , cascade = CascadeType.ALL)
    private Token token;

    @Data
    public static class UserID {
        private Long id;
    }

    public User() {
    }

}

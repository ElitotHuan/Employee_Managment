package com.example.Employee_Managment.models;

import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@Validated
@Entity
@Table(name = "login")
public class Login {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(columnDefinition = "TEXT")
    @NotEmpty
    private String username;

    @Column(columnDefinition = "TEXT")
    @NotEmpty
    private String password;

    private Date create_date;

    private Date exp_date;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id" , referencedColumnName = "user_id")
    private User User_db;


    public Login() {
    }

    public Login(String username, String password, Date create_date, Date exp_date , User user_db) {
        this.username = username;
        this.password = password;
        this.create_date = create_date;
        this.exp_date = exp_date;
        this.User_db =  user_db;
    }
}

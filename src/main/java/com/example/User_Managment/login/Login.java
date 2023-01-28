package com.example.User_Managment.login;

import com.example.User_Managment.user.User;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.*;
import java.util.Date;

@Data
@Entity
@Table(name = "login")
public class Login {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @XmlElement(name = "username")
    @Column(columnDefinition = "TEXT")
    @NotEmpty(message = " is required")
    private String username;

    @XmlElement(name = "password")
    @Column(columnDefinition = "TEXT")
    @NotEmpty(message = " is required")
    private String password;

    @NotNull
    private Date create_date;

    @NotNull
    private Date exp_date;

    private Date update_date;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;

    @Data
    public static class LoginRequest {
        @NotEmpty(message = " is required")
        private String username;
        @NotEmpty(message = " is required")
        private String password;
    }

    @Data
    public static class LoginRespone {
        private String message;
        private String accessToken;
        private String refreshToken;

        public LoginRespone(String message, String accessToken , String refreshToken) {
            this.message = message;
            this.accessToken = accessToken;
            this.refreshToken = refreshToken;
        }
    }

    public Login() {
    }

    public Login(String username, String password, Date create_date, Date exp_date, Date update_date, User user) {
        this.username = username;
        this.password = password;
        this.create_date = create_date;
        this.exp_date = exp_date;
        this.update_date = update_date;
        this.user = user;
    }
}

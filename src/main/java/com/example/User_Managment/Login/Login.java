package com.example.User_Managment.Login;

import com.example.User_Managment.User.User;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.*;
import java.util.Date;

@XmlRootElement(name = "login")
@XmlAccessorType(XmlAccessType.NONE)
@Data
@Validated
@Entity
@Table(name = "login")
public class Login {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @XmlElement(name = "username")
    @Column(columnDefinition = "TEXT")
    @NotEmpty(message = "username is required")
    private String username;

    @XmlElement(name = "password")
    @Column(columnDefinition = "TEXT")
    @NotEmpty(message = "password is required")
    private String password;


    private Date create_date;

    private Date exp_date;

    private Date update_date;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;

//    @Data
//    public static class LoginRequest {
//        @NotEmpty(message = "username is required")
//        private String username;
//        @NotEmpty(message = "password is required")
//        private String password;
//    }

    @Data
    public static class LoginRespone {
        private String message;
        private String accessToken;

        public LoginRespone(String message, String accessToken) {
            this.message = message;
            this.accessToken = accessToken;
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

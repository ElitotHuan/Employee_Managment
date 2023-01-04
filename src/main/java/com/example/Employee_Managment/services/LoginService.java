package com.example.Employee_Managment.services;

import com.example.Employee_Managment.models.Login;
import com.example.Employee_Managment.repositories.LoginRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    @Autowired
    LoginRepository loginRepository;


    public Login findAccount(String username , String password) {
        Login user = loginRepository.findByUsernameAndPasssword(username , password);
        return user;
    }


    public Login add(Login login) {
        return loginRepository.save(login);
    }



}

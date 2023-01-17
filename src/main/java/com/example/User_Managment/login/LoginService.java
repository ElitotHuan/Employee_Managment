package com.example.User_Managment.login;

import com.example.User_Managment.response_handler.ErrorRespone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class LoginService {

    private final Logger logger = LoggerFactory.getLogger(LoginService.class);
    @Autowired
    private LoginRepository loginRepository;

    public Object validateAccount(Login.LoginRequest request) {
        logger.info("Find and validate account...");
        Login account = loginRepository.findAccount(request.getUsername(), request.getPassword());
        return account != null ? (!account.getExp_date().before(new Date()) ? account : new ErrorRespone("Your account has expired please contact your system administrator", 403))
                : new ErrorRespone("Username or password is incorrect", 401);
    }


}

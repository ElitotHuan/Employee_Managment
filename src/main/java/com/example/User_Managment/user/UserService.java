package com.example.User_Managment.user;

import com.example.User_Managment.login.Login;
import com.example.User_Managment.login.LoginRepository;
import com.example.User_Managment.response_handler.ErrorRespone;
import com.example.User_Managment.response_handler.SuccessRespone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LoginRepository loginRepository;

    private final Logger logger = LoggerFactory.getLogger(UserService.class);


    public Object getUsers(String id) {
        if (id == null) {
            logger.info("Getting User list...");
            List<UserDTO> list = userRepository.getAllUsers();

            if (list.isEmpty()) {
                return new SuccessRespone("There are no users");
            }

            return list;
        } else {
            User u = userRepository.getReferenceById(Long.valueOf(id));
            logger.info("Return employee success");
            return new UserDTO(u.getUserId(), u.getName(), u.getAge(), u.getPosition(), u.getSalary());
        }
    }

    public Object addUser(User user) {
        logger.info("Recieving data from client...");
        if (userRepository.existsByUsername(user.getUsername())) {
            logger.warn("Username already existed");
            return new ErrorRespone("This username is already taken", HttpStatus.CONFLICT.value());
        } else {
            Login login1 = new Login(user.getUsername(), user.getPassword(),
                    new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis() + 3 *  365 * 24 * 60 * 60 * 1000), null ,user);
            userRepository.save(user);
            loginRepository.save(login1);
            logger.info("User has been added");
            return new SuccessRespone("Added successfully");
        }
    }

    public Object updateUser(Long id, User employee) {
        logger.info("get User with id = " + id + "...");
        User updateEmployee = userRepository.getReferenceById(id);
        /*
            Ony update the information of the employee except the username
        */
        if (updateEmployee.getUsername().equalsIgnoreCase(employee.getUsername())) {
            setUserInfo(employee, updateEmployee);
            return new SuccessRespone("Update successfully");
        } else {
            //Update the username and other information
            if (userRepository.existsByUsername(employee.getUsername())) {
                logger.warn("Username already existed");
                return new ErrorRespone("This username is already taken", HttpStatus.CONFLICT.value());
            } else {
                setUserInfo(employee, updateEmployee);
                logger.info("Data has been updated");
                return new SuccessRespone("Updated successfully");
            }
        }
    }


    public Object updatePassword(Long user_id, String password) {
        logger.info("get User with id = " + user_id + "...");
        User getUserInfo = userRepository.getReferenceById(user_id);
        Date updateDate = new Date();

        getUserInfo.setPassword(password);
        getUserInfo.getLogin().setPassword(password);
        getUserInfo.getLogin().setUpdate_date(updateDate);

        userRepository.save(getUserInfo);
        logger.info("Password has been updated");
        return new SuccessRespone("Updated password successfully");
    }

    public Object deleteUser(Long id) {
        User employee = userRepository.getReferenceById(id);
        userRepository.delete(employee);
        logger.info("User: " + employee.getName() + " has been deleted");
        return new SuccessRespone("Delete successfully");
    }

    private void setUserInfo(User employee, User updateEmployee) {
        updateEmployee.setName(employee.getName());
        updateEmployee.setAge(employee.getAge());
        updateEmployee.setUsername(employee.getUsername());
        updateEmployee.setPassword(employee.getPassword());
        updateEmployee.setPosition(employee.getPosition());
        updateEmployee.setSalary(employee.getSalary());
        userRepository.save(updateEmployee);
    }

}

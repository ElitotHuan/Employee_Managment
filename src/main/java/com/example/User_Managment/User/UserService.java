package com.example.User_Managment.User;

import com.example.User_Managment.Login.Login;
import com.example.User_Managment.Login.LoginRepository;
import com.example.User_Managment.response_handler.ErrorRespone;
import com.example.User_Managment.response_handler.SuccessRespone;
import org.hibernate.PropertyValueException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
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
            User user1 = userRepository.save(user);
            Login login1 = new Login(user.getUsername(), user.getPassword(),
                    new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis() + 3L * 12 * 365 * 24 * 60 * 60 * 1000), null, user1);
            userRepository.save(user1);
            loginRepository.save(login1);
            return new SuccessRespone("Added successfully");
        }
    }

    public Object updateUser(Long id, User employee) {

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


//    public ResponeObject updatePassword(Long employ_id, String password) {
//        User employee = employeeRepository.getReferenceById(employ_id);
//        PasswordInfo passwordInfo = getPasswordInfoByEmployId(employee);
//        Date update_date = new Date();
//
//        employee.setPassword(password);
//        passwordInfo.setPassword(password);
//        passwordInfo.setUpdate_date(update_date);
//
//        employeeRepository.save(employee);
//        passwordRepository.save(passwordInfo);
//        return new ResponeObject("Update password successfully");
//    }

    public Object deleteUser(Long id) {
        User employee = userRepository.getReferenceById(id);
        userRepository.delete(employee);
        logger.info("User: " + employee.getName() + " has been deleted");
        return new SuccessRespone("Delete successfully");
    }


    public Boolean checkUserIdExist(Long userId) {
        return userRepository.existsByUserId(userId);
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

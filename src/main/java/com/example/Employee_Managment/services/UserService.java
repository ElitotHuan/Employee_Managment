package com.example.Employee_Managment.services;

import com.example.Employee_Managment.dto.UserDTO;
import com.example.Employee_Managment.models.Login;
import com.example.Employee_Managment.models.User;
import com.example.Employee_Managment.repositories.UserRepository;
import com.example.Employee_Managment.response_handler.error_response.Error;
import com.example.Employee_Managment.response_handler.error_response.ResponeObject;
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
    private LoginService loginService;


    private final Logger logger = LoggerFactory.getLogger(UserService.class);

    private List<User> list;

    public List<UserDTO> getAll() {
        logger.info("Getting User list...");
        List<User> list = userRepository.findAll();
        List<UserDTO> result = new ArrayList<>();
        try {
            if (list.isEmpty()) {
                logger.warn("List is empty");
                return result;
            }
        } catch (NullPointerException e) {
            logger.error("List is null --> " + e.getMessage());
            return null;
        }


        for (User e : list) {
            UserDTO employeeDTO = new UserDTO(e.getUserId(), e.getName(), e.getAge(), e.getPosition(), e.getSalary());
            result.add(employeeDTO);
        }
        logger.info("Return list success");
        return result;
    }

    public UserDTO getUser(Long id) {
        try {
            User employeeWithId = userRepository.findById(id).get();
            UserDTO result = new UserDTO(employeeWithId.getUserId(), employeeWithId.getName(), employeeWithId.getAge(),
                    employeeWithId.getPosition(), employeeWithId.getSalary());
            logger.info("Return employee success");
            return result;
        } catch (NoSuchElementException e) {
            logger.error("User doesn't exit");
            return null;
        }
    }

    public ResponeObject addUser(User user) {
        logger.info("Recieving data from client...");
        try {
            if (userRepository.existsByUsername(user.getUsername())) {
                logger.warn("Username already existed");
                return new ResponeObject("Error found", new Error("Insert failed",
                        "This username is already taken", HttpStatus.CONFLICT.value()));
            } else {
                User user1 = userRepository.save(user);
                Login login1 = new Login(user.getUsername() , user.getPassword() ,
                        new Date(System.currentTimeMillis()) , new Date(System.currentTimeMillis() + 3 * 12 * 365 * 24 * 60 * 60 * 1000) , user1);
                userRepository.save(user1);
                loginService.add(login1);
                logger.info(user1 + " has been added to database");
                return new ResponeObject("Added successfully");
            }
        } catch (PropertyValueException | DataIntegrityViolationException e) {
            logger.error("Cannot add data because " + e);
            return new ResponeObject("Something went wrong", new Error("JSON parse error",
                    e.getLocalizedMessage(), HttpStatus.BAD_REQUEST.value()));
        }
    }

    public ResponeObject updateUser(Long id, User employee) {

        User updateEmployee = userRepository.getReferenceById(id);
        /*
            Ony update the information of the employee except the username
        */
        if (updateEmployee.getUsername().equalsIgnoreCase(employee.getUsername())) {
            updateEmployee.setName(employee.getName());
            updateEmployee.setAge(employee.getAge());
            updateEmployee.setUsername(employee.getUsername());
            updateEmployee.setPassword(employee.getPassword());
            updateEmployee.setPosition(employee.getPosition());
            updateEmployee.setSalary(employee.getSalary());
            userRepository.save(updateEmployee);
            logger.info("Data has been updated");
            return new ResponeObject("Update successfully");
        } else {
            //Update the username and other information
            if (userRepository.existsByUsername(employee.getUsername())) {
                logger.warn("Username already existed");
                return new ResponeObject("Error found", new Error("Update failed",
                        "This username is already taken", HttpStatus.CONFLICT.value()));
            } else {
                updateEmployee.setName(employee.getName());
                updateEmployee.setAge(employee.getAge());
                updateEmployee.setUsername(employee.getUsername());
                updateEmployee.setPassword(employee.getPassword());
                updateEmployee.setPosition(employee.getPosition());
                updateEmployee.setSalary(employee.getSalary());
                userRepository.save(updateEmployee);
                logger.info("Data has been updated");
                return new ResponeObject("Updated successfully");
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

    public ResponeObject deleteUser(Long id) {
        User employee = userRepository.getById(id);
        userRepository.delete(employee);
        logger.info("User: " + employee.getName() + " has been deleted");
        return new ResponeObject("Delete successfully");
    }


    public Boolean checkIdExist(Long userId) {
        return userRepository.existsByUserId(userId);
    }


}

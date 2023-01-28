package com.example.User_Managment.user;

import com.example.User_Managment.exceptions_handler.customs_exception.UserExistedException;
import com.example.User_Managment.login.Login;
import com.example.User_Managment.login.LoginRepository;
import com.example.User_Managment.response_handler.SuccessRespone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private final Logger logger = LoggerFactory.getLogger(UserService.class);


    public Object getUsers(String id) {
        if (id == null) {
            logger.info("Getting User list...");
            List<UserDTO> list = userRepository.getAllUsers();
            return list;
        } else {
            User u = userRepository.getReferenceById(Long.valueOf(id));
            logger.info("Return employee success");
            return new UserDTO(u.getUserId(), u.getName(), u.getAge(), u.getPosition(), u.getSalary());
        }
    }

    public SuccessRespone addUser(User user) {
        logger.info("Recieving data from client...");
        if (userRepository.existsByUsername(user.getUsername())) {
            logger.warn("Username already existed");
            throw new UserExistedException();
        } else {
            Login login1 = new Login(user.getUsername(), user.getPassword(),
                    new Date(), new Date(System.currentTimeMillis() + 3 * 365 * 24 * 60 * 60 * 1000L), null, user);
            user.setLogin(login1);
            userRepository.save(user);
            logger.info("User has been added");
            return new SuccessRespone("Added successfully");
        }
    }

    public SuccessRespone updateUser(Long id, User employee) {
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
                logger.warn("Username: " + employee.getUsername() + " already existed");
                throw new UserExistedException("Username: " + employee.getUsername() + " already existed");
            } else {
                setUserInfo(employee, updateEmployee);
                logger.info("Data has been updated");
                return new SuccessRespone("Updated successfully");
            }
        }
    }


    public SuccessRespone updatePassword(Long user_id, String password) {
        logger.info("get User with id = " + user_id + "...");
        User getUserInfo = userRepository.getReferenceById(user_id);

        getUserInfo.setPassword(password);
        getUserInfo.getLogin().setPassword(password);
        getUserInfo.getLogin().setUpdate_date(new Date());

        userRepository.save(getUserInfo);
        logger.info("Password has been updated");
        return new SuccessRespone("Updated password successfully");
    }

    public SuccessRespone updateAccountDate(Long user_id , int numberOfYear) {
        logger.info("get User with id = " + user_id + "...");
        User getUserInfo = userRepository.getReferenceById(user_id);

        Date newExpDate = new Date(getUserInfo.getLogin().getExp_date().getTime() + numberOfYear * 365 * 24 * 60 * 60 * 1000L);
        getUserInfo.getLogin().setExp_date(newExpDate);

        userRepository.save(getUserInfo);
        logger.info("Expired date has been updated");
        return new SuccessRespone("Updated account expired date successfully");
    }

    public SuccessRespone deleteUser(Long id) {
        User employee = userRepository.getReferenceById(id);
        userRepository.delete(employee);
        logger.info("User: " + employee.getName() + " has been deleted");
        return new SuccessRespone("Delete successfully");
    }

    private void setUserInfo(User user, User updateUser) {
        updateUser.setName(user.getName());
        updateUser.setAge(user.getAge());
        updateUser.setUsername(user.getUsername());
        updateUser.setPassword(user.getPassword());
        updateUser.setPosition(user.getPosition());
        updateUser.setSalary(user.getSalary());
        updateUser.setRole(user.getRole());
        userRepository.save(updateUser);
    }

}

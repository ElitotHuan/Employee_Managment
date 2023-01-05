package com.example.Employee_Managment.controller;

import com.example.Employee_Managment.dto.UserDTO;
import com.example.Employee_Managment.models.User;
import com.example.Employee_Managment.models.Token;
import com.example.Employee_Managment.response_handler.error_response.Error;
import com.example.Employee_Managment.response_handler.error_response.ResponeObject;
import com.example.Employee_Managment.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService services;


    Token token = new Token();


    @GetMapping("/get")
    public ResponseEntity<?> getUser(@RequestParam(required = false) Long id,
                                     @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authtoken) {

        if (authtoken == null) {
            ResponeObject respone = new ResponeObject("Error found", new Error("Access denied",
                    "Please provide token", 403));
            return ResponseEntity.status(respone.getError().getStatus()).body(respone);
        }

        if (token.validateToken(authtoken)) {
            if (services.checkIdExist(token.getUserIdFromToken(authtoken))) {
                if (id == null) {
                    List<UserDTO> list = services.getAll();

                    if (list.isEmpty()) {
                        return ResponseEntity.ok().body("There are no employees");
                    }

                    if (list == null) {
                        NullPointerException e = new NullPointerException();
                        ResponeObject respone = new ResponeObject("Error found", new Error(e.getMessage(),
                                e.getCause().toString(), HttpStatus.BAD_REQUEST.value()));
                        return ResponseEntity.status(respone.getError().getStatus()).body(respone);
                    }

                    return ResponseEntity.ok(list);
                } else {
                    UserDTO employee = services.getUser(id);
                    if (employee == null) {
                        return ResponseEntity.status(200).body("User doesn't exit");
                    }
                    return ResponseEntity.ok(employee);
                }
            } else {
                ResponeObject respone = new ResponeObject("Error found", new Error("Access denied",
                        "Token contains a User-id that doesn't exist", 401));
                return ResponseEntity.status(respone.getError().getStatus()).body(respone);
            }
        }
        ResponeObject respone = new ResponeObject("Error found", new Error("Access denied",
                "Token is expired please login again", 403));
        return ResponseEntity.status(respone.getError().getStatus()).body(respone);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addUser(@Valid @RequestBody User user,
                                     @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authtoken) {
        if (authtoken == null) {
            ResponeObject respone = new ResponeObject("Error found", new Error("Access denied",
                    "Please provide token", 403));
            return ResponseEntity.status(respone.getError().getStatus()).body(respone);
        }

//        if (token.validateToken(authtoken)) {
//
//        } else {
//            ResponeObject respone = new ResponeObject("Error found", new Error("Access denied",
//                    "User id doesn't exist", 401));
//            return ResponseEntity.status(respone.getError().getStatus()).body(respone);
//        }

        ResponeObject respone = services.addUser(user);
        return ResponseEntity.status(respone.getError().getStatus()).body(respone);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateUser(@Valid @RequestBody User user,
                                        @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authtoken) {
        if (authtoken == null) {
            ResponeObject respone = new ResponeObject("Error found", new Error("Access denied",
                    "Please provide token", 403));
            return ResponseEntity.status(respone.getError().getStatus()).body(respone);
        }

//        if (token.validateToken(authtoken)) {
//            ResponeObject respone = services.updateUser(user.getUserId(), user);
//            return ResponseEntity.status(respone.getError().getStatus()).body(respone);
//        } else {
//            ResponeObject respone = new ResponeObject("Error found", new Error("Access denied",
//                    "Token expired please login", 403));
//            return ResponseEntity.status(respone.getError().getStatus()).body(respone);
//        }

        return null;
    }

//    @PutMapping("/update/password")
//    public ResponseEntity<?> updatePassword(@Valid @RequestBody User.PasswordUpdate update) {
//        ResponeObject respone = services.updatePassword(update.getId(), update.getNewPassword());
//        return ResponseEntity.status(respone.getError().getStatus()).body(respone);
//    }
//

    @DeleteMapping("/delete")
    public ResponseEntity<?> removeUser(@RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authtoken
            , @RequestBody User.UserID id) {
        if (authtoken == null) {
            ResponeObject respone = new ResponeObject("Error found", new Error("Access denied",
                    "Please provide token", 403));
            return ResponseEntity.status(respone.getError().getStatus()).body(respone);
        }

        if (token.validateToken(authtoken)) {
            if(services.checkIdExist(token.getUserIdFromToken(authtoken))) {
                ResponeObject respone = services.deleteUser(id.getId());
                return ResponseEntity.status(respone.getError().getStatus()).body(respone);
            } else {
                ResponeObject respone = new ResponeObject("Error found", new Error("Access denied",
                        "Token contains a User-id that doesn't exist", 401));
                return ResponseEntity.status(respone.getError().getStatus()).body(respone);
            }

        } else {
            ResponeObject respone = new ResponeObject("Error found", new Error("Access denied",
                    "Token expired please login", 403));
            return ResponseEntity.status(respone.getError().getStatus()).body(respone);
        }

    }

}

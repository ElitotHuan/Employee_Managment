package com.example.User_Managment.User.controller;

import com.example.User_Managment.User.dto.UserDTO;
import com.example.User_Managment.User.models.User;
import com.example.User_Managment.Authenticate.models.Token;
import com.example.User_Managment.response_handler.Error;
import com.example.User_Managment.response_handler.ResponeObject;
import com.example.User_Managment.User.services.UserService;
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

        if (token.validateToken(authtoken)) {
            if (services.checkUserIdExist(token.getUserIdFromToken(authtoken))) {
                if (id == null) {
                    try {
                        List<UserDTO> list = services.getAll();
                        if (list.isEmpty()) {
                            return ResponseEntity.ok().body(new ResponeObject("There are no employees"));
                        }
                        return ResponseEntity.ok(list);
                    } catch (NullPointerException e) {
                        ResponeObject respone = new ResponeObject("Error found", new Error(e.getMessage(), HttpStatus.BAD_REQUEST.value()));
                        return ResponseEntity.status(respone.getError().getStatus()).body(respone);
                    }

                } else {
                    UserDTO employee = services.getUser(id);
                    if (employee == null) {
                        return ResponseEntity.status(200).body(new ResponeObject("User doesn't exit"));
                    }
                    return ResponseEntity.ok(employee);
                }
            } else {
                ResponeObject respone = new ResponeObject("Error found", new Error("Access denied", 401));
                return ResponseEntity.status(respone.getError().getStatus()).body(respone);
            }
        }
        ResponeObject respone = new ResponeObject("Error found", new Error("Access denied", 403));
        return ResponseEntity.status(respone.getError().getStatus()).body(respone);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addUser(@Valid @RequestBody User user,
                                     @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authtoken) {

        if (token.validateToken(authtoken)) {
            if (services.checkUserIdExist(token.getUserIdFromToken(authtoken))) {
                ResponeObject respone = services.addUser(user);
                return ResponseEntity.status(200).body(respone);
            } else {
                ResponeObject respone = new ResponeObject("Error found", new Error("Access denied:", 401));
                return ResponseEntity.status(respone.getError().getStatus()).body(respone);
            }
        } else {
            ResponeObject respone = new ResponeObject("Error found", new Error("Access denied", 401));
            return ResponseEntity.status(respone.getError().getStatus()).body(respone);
        }

    }

    @PutMapping("/update")
    public ResponseEntity<?> updateUser(@Valid @RequestBody User user,
                                        @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authtoken) {

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
    public ResponseEntity<?> removeUser(@Valid @RequestBody User.UserID id,
                                        @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authtoken) {

        if (token.validateToken(authtoken)) {
            if (services.checkUserIdExist(token.getUserIdFromToken(authtoken))) {
                ResponeObject respone = services.deleteUser(id.getId());
                return ResponseEntity.status(respone.getError().getStatus()).body(respone);
            } else {
                ResponeObject respone = new ResponeObject("Error found", new Error(
                        "Access denied", 401));
                return ResponseEntity.status(respone.getError().getStatus()).body(respone);
            }

        } else {
            ResponeObject respone = new ResponeObject("Error found", new Error("Access denied", 403));
            return ResponseEntity.status(respone.getError().getStatus()).body(respone);
        }

    }

}

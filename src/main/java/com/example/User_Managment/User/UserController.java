package com.example.User_Managment.User;

import com.example.User_Managment.Authenticate.Token;
import com.example.User_Managment.response_handler.ErrorRespone;
import com.example.User_Managment.response_handler.SuccessRespone;
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
    UserService userService;


    Token token = new Token();

    ErrorRespone errorRespone;


    @GetMapping("/get")
    public ResponseEntity<?> getUser(@RequestParam(required = false) String id,
                                     @RequestHeader(value = HttpHeaders.AUTHORIZATION) String authtoken) {
        if (userService.checkUserIdExist(token.getUserIdFromToken(authtoken))) {
            Object respone = userService.getUsers(id.describeConstable());
            if (respone instanceof ErrorRespone) {
                return ResponseEntity.status(((ErrorRespone) respone).getStatus()).body(respone);
            }
            return ResponseEntity.status(200).body(respone);
        } else {
            errorRespone = new ErrorRespone("Unauthorized Access!", HttpStatus.UNAUTHORIZED.value());
            return ResponseEntity.status(errorRespone.getStatus()).body(errorRespone);
        }
    }

    @PostMapping("/add")
    public ResponseEntity<?> addUser(@RequestBody @Valid User user,
                                     @RequestHeader(value = HttpHeaders.AUTHORIZATION) String authtoken) {

        if (userService.checkUserIdExist(token.getUserIdFromToken(authtoken))) {
            Object respone = userService.addUser(user);
            if (respone instanceof ErrorRespone) {
                return ResponseEntity.status(((ErrorRespone) respone).getStatus()).body(respone);
            }
            return ResponseEntity.status(200).body(respone);
        } else {
            errorRespone = new ErrorRespone("Unauthorized Access!", 401);
            return ResponseEntity.status(errorRespone.getStatus()).body(errorRespone.getStatus());
        }
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateUser(@RequestBody @Valid User user,
                                        @RequestHeader(value = HttpHeaders.AUTHORIZATION) String authtoken) {

        if (userService.checkUserIdExist(token.getUserIdFromToken(authtoken))) {
            Object respone = userService.updateUser(user.getUserId(), user);
            if (respone instanceof ErrorRespone) {
                return ResponseEntity.status(((ErrorRespone) respone).getStatus()).body(respone);
            }
            return ResponseEntity.status(200).body(respone);
        } else {
            errorRespone = new ErrorRespone("Unauthorized Access!", 401);
            return ResponseEntity.status(errorRespone.getStatus()).body(errorRespone.getStatus());
        }

    }

//    @PutMapping("/update/password")
//    public ResponseEntity<?> updatePassword(@Valid @RequestBody User.PasswordUpdate update) {
//        ResponeObject respone = services.updatePassword(update.getId(), update.getNewPassword());
//        return ResponseEntity.status(respone.getError().getStatus()).body(respone);
//    }
//

    @DeleteMapping("/delete")
    public ResponseEntity<?> removeUser(@Valid @RequestBody User.UserID id,
                                        @RequestHeader(value = HttpHeaders.AUTHORIZATION) String authtoken) {


        if (userService.checkUserIdExist(token.getUserIdFromToken(authtoken))) {
            Object respone = userService.deleteUser(id.getId());
            return ResponseEntity.ok().body(respone);
        } else {
            errorRespone = new ErrorRespone("Unauthorized Access!", 401);
            return ResponseEntity.status(errorRespone.getStatus()).body(errorRespone.getStatus());
        }


    }

}

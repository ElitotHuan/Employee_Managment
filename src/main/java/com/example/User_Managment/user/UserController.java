package com.example.User_Managment.user;

import com.example.User_Managment.authenticate.TokenService;
import com.example.User_Managment.response_handler.ErrorRespone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    private TokenService tokenService;

    @GetMapping("/get")
    public ResponseEntity<?> getUser(@RequestParam(required = false) String id,
                                     @RequestHeader(value = HttpHeaders.AUTHORIZATION) String authtoken) {
        tokenService.validateToken(authtoken);
        Object response = userService.getUsers(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addUser(@RequestBody @Valid User user,
                                     @RequestHeader(value = HttpHeaders.AUTHORIZATION) String authtoken) {
        tokenService.validateToken(authtoken);
        tokenService.checkRole(authtoken);
        Object response = userService.addUser(user);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateUser(@RequestBody @Valid User user,
                                        @RequestHeader(value = HttpHeaders.AUTHORIZATION) String authtoken) {
        tokenService.validateToken(authtoken);
        tokenService.checkRole(authtoken);
        Object response = userService.updateUser(user.getUserId(), user);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/update/password")
    public ResponseEntity<?> updatePassword(@Valid @RequestBody User.PasswordUpdate update,
                                            @RequestHeader(value = HttpHeaders.AUTHORIZATION) String authtoken) {
        tokenService.validateToken(authtoken);
        tokenService.checkRole(authtoken);
        Object response = userService.updatePassword(update.getId(), update.getNewPassword());
        return ResponseEntity.ok(response);
    }

    @PutMapping("/update/expiredDate")
    public ResponseEntity<?> extendExpiredDate(@Valid @RequestBody User.ExpUpdate update,
                                               @RequestHeader(value = HttpHeaders.AUTHORIZATION) String authtoken) {
        tokenService.validateToken(authtoken);
        tokenService.checkRole(authtoken);
        Object response = userService.updateAccountDate(update.getId(), update.getNumberOfYear());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> removeUser(@Valid @RequestBody User.UserID id,
                                        @RequestHeader(value = HttpHeaders.AUTHORIZATION) String authtoken) {
        tokenService.validateToken(authtoken);
        tokenService.checkRole(authtoken);
        Object response = userService.deleteUser(id.getId());
        return ResponseEntity.ok(response);
    }

}

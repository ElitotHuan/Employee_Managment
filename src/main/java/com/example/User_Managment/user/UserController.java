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
        String userRole = tokenService.getRoleFromToken(authtoken);
        Object respone = userService.getUsers(id);
        return ResponseEntity.ok(respone);
    }

    @PostMapping("/add")
    @ResponseBody
    public ResponseEntity<?> addUser(@RequestBody @Valid User user,
                                     @RequestHeader(value = HttpHeaders.AUTHORIZATION) String authtoken) {
        tokenService.validateToken(authtoken);
        String userRole = tokenService.getRoleFromToken(authtoken);
        Object respone = userService.addUser(user);
        return ResponseEntity.ok(respone);

    }

    @PutMapping("/update")
    public ResponseEntity<?> updateUser(@RequestBody @Valid User user,
                                        @RequestHeader(value = HttpHeaders.AUTHORIZATION) String authtoken) {
        tokenService.validateToken(authtoken);
        Object respone = userService.updateUser(user.getUserId(), user);
        return ResponseEntity.ok(respone);
    }

    @PutMapping("/update/password")
    public ResponseEntity<?> updatePassword(@Valid @RequestBody User.PasswordUpdate update,
                                            @RequestHeader(value = HttpHeaders.AUTHORIZATION) String authtoken) {
        tokenService.validateToken(authtoken);
        Object respone = userService.updatePassword(update.getId(), update.getNewPassword());
        return ResponseEntity.ok(respone);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> removeUser(@Valid @RequestBody User.UserID id,
                                        @RequestHeader(value = HttpHeaders.AUTHORIZATION) String authtoken) {
        tokenService.validateToken(authtoken);
        Object respone = userService.deleteUser(id.getId());
        return ResponseEntity.ok(respone);
    }

}

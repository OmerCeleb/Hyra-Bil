package com.saferent1.controller;

import com.saferent1.domain.User;
import com.saferent1.dto.UserDTO;
import com.saferent1.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController //Restfulapi
@RequestMapping("/user") // --> user endpoint
public class UserController {

    private final UserService userService;


    public UserController(UserService userService) {
        this.userService = userService;
    }

    //*************************************************************************************
    //!!! getAllUser
    @GetMapping("/auth/all") //http://localhost:8080/user/auth/all
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> allUser = userService.getAllUsers();
        return ResponseEntity.ok(allUser);
    }

    //*************************************************************************************
     //!!! Informationen om användaren som loggade in på systemet ...
    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('CUSTOMER')")
    public ResponseEntity<UserDTO> getUser() {
        UserDTO userDTO = userService.getPrincipal();
        return ResponseEntity.ok(userDTO);
    }


}

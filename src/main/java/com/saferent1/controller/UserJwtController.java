package com.saferent1.controller;

import com.saferent1.dto.response.SfResponse;
import com.saferent1.security.jwt.JwtUtils;
import com.saferent1.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class UserJwtController {

    //!!! Endast inloggning och registrering kommer att utföras i denna klass.

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;


    //!!! Register
    @PostMapping("/register")
    public ResponseEntity<SfResponse> registerUser(@Valid ){

    }
}

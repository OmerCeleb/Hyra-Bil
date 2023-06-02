package com.saferent1.controller;

import com.saferent1.dto.request.RegisterRequest;
import com.saferent1.dto.response.ResponseMessage;
import com.saferent1.dto.response.SfResponse;
import com.saferent1.security.jwt.JwtUtils;
import com.saferent1.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    public ResponseEntity<SfResponse> registerUser(@Valid
                                                   @RequestBody RegisterRequest request) {

        userService.saveUser(request);
        SfResponse response = new SfResponse();
        response.setMessage(ResponseMessage.REGISTER_RESPONSE_MESSAGE);
        response.setSucces(true);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    //Login
    @PostMapping("/login")
    public ResponseEntity<>



}

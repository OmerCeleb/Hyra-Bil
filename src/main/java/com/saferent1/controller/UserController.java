package com.saferent1.controller;

import com.saferent1.domain.User;
import com.saferent1.dto.UserDTO;
import com.saferent1.dto.request.UpdatePasswordRequest;
import com.saferent1.dto.request.UserUpdateRequest;
import com.saferent1.dto.response.ResponseMessage;
import com.saferent1.dto.response.SfResponse;
import com.saferent1.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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


    //!!! GetAllUserWithPage
    @GetMapping("/auth/pages")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<UserDTO>> getAllUsersByPage(
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @RequestParam("sort") String prop,
            @RequestParam(value = "direction",
                    required = false,
                    defaultValue = "DESC") Sort.Direction direction) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, prop));

        Page<UserDTO> userDTOPage = userService.getUserByPage(pageable);

        return ResponseEntity.ok(userDTOPage);

    }


    //!!! GetUserById
    @GetMapping("/{id}/auth")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {

        UserDTO userDTO = userService.getUserById(id);
        return ResponseEntity.ok(userDTO);

    }

    //*************************************************************************************

    //!!! Update Password
    @PatchMapping("auth")
    @PreAuthorize("hasRole('ADMIN') or hasRole('CUSTOMER')")
    public ResponseEntity<SfResponse> updatePassword(@Valid @RequestBody
                                                     UpdatePasswordRequest passwordRequest) {

        userService.updatePassword(passwordRequest);

        SfResponse response = new SfResponse();
        response.setMessage(ResponseMessage.PASSWORD_CHANGED_RESPONSE_MESSAGE);

        response.setSucces(true);

        return ResponseEntity.ok(response);
    }


    //*************************************************************************************

    //!!! Update User
    @PutMapping()
    @PreAuthorize("hasRole('ADMIN') or hasRole('CUSTOMER')")
    public ResponseEntity<SfResponse> updateUser(
            @Valid @RequestBody UserUpdateRequest userUpdateRequest) {

        userService.updateUser(userUpdateRequest);

        SfResponse response = new SfResponse();
        response.setMessage(ResponseMessage.USER_UPDATE_RESPONSE_MESSAGE);
        response.setSucces(true);

        return ResponseEntity.ok(response);
    }


}

package com.saferent1.service;

import com.saferent1.domain.Role;
import com.saferent1.domain.User;
import com.saferent1.domain.enums.RoleType;
import com.saferent1.dto.UserDTO;
import com.saferent1.dto.request.RegisterRequest;
import com.saferent1.dto.request.UpdatePasswordRequest;
import com.saferent1.exception.BadRequestException;
import com.saferent1.exception.ConflictException;
import com.saferent1.exception.ResourceNotFoundException;
import com.saferent1.exception.message.ErrorMessage;
import com.saferent1.mapper.UserMapper;
import com.saferent1.repository.UserRepository;
import com.saferent1.security.SecurityUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserService {


    private UserRepository userRepository;

    private UserMapper userMapper;
    private RoleService roleService;

    private PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, RoleService roleService,
                       @Lazy PasswordEncoder passwordEncoder, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    public User getUserByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new ResourceNotFoundException(String.format(ErrorMessage.USER_NOT_FOUND_EXCEPTION, email)));

        return user;
    }


    public void saveUser(RegisterRequest request) {
        //!!! Finns e-postmeddelandet från DTO i systemet tidigare???
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ConflictException(String.format(ErrorMessage.EMAIL_ALDREADY_EXIST_MESSAGE, request.getEmail())
            );
        }

        // !!! Jag tilldelar den nya användarens rollinformation som standardkund Jag kan inte registrera någon utan rollinformation till databasen
        Role role = roleService.findByType(RoleType.ROLE_CUSTOMER);
        Set<Role> roles = new HashSet<>();
        roles.add(role);

        //!!! Lösenordet encode innan du går till db
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        //!!! ställer in nödvändig information om den nya användaren och skickar den till DB
        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setAddress(request.getAddress());
        user.setPassword(encodedPassword);
        user.setZipCode(request.getZipCode());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setRoles(roles);

        userRepository.save(user);


    }


    //*************************************************************************
    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        List<UserDTO> userDTOS = userMapper.map(users);

        return userDTOS;
    }

    //*************************************************************************

    public UserDTO getPrincipal() {
        User user = getCurrentUser();
        UserDTO userDTO = userMapper.userToUserDTO(user);
        return userDTO;
    }

    public User getCurrentUser() {
        String email = SecurityUtils.getCurrentUserLogin().orElseThrow(() ->
                new ResourceNotFoundException(ErrorMessage.PRINCIPAL_FOUND_MESSAGE));

        User user = getUserByEmail(email);
        return user;

    }

    //**************************

    public Page<UserDTO> getUserByPage(Pageable pageable) {

        Page<User> userPage = userRepository.findAll(pageable);
        return getUserDTOPage(userPage);

    }

    private Page<UserDTO> getUserDTOPage(Page<User> userPage) {
        return userPage.map(
                user -> userMapper.userToUserDTO(user)
        );
    }

    //*************************************************************************

    public UserDTO getUserById(Long id) {

        User user = userRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(
                        String.format(ErrorMessage.RESOURCE_NOT_FOUND_EXCEPTION, id)));

        return userMapper.userToUserDTO(user);
    }


    //*************************************************************************

    public void updatePassword(UpdatePasswordRequest passwordRequest) {

        User user = getCurrentUser();

        // !!! builtIn??
        if (user.getBuiltIn()) {
            throw new BadRequestException(ErrorMessage.NOT_PERMITTED_METHOD_MESSAGE);
        }

        // !!! Är det gamla lösenordet som angetts i formulärdelen korrekt?
        if (!passwordEncoder.matches(passwordRequest.getOldPassword(), user.getPassword())) {
            throw new BadRequestException(ErrorMessage.PASSWORD_NOT_MATCHED);
        }

        // !!! nytt inkommande lösenord kommer att kodas
        String hashedPassword = passwordEncoder.encode(passwordRequest.getNewPassword());
        user.setPassword(hashedPassword);

        userRepository.save(user);


    }

    //*************************************************************************





}

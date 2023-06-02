package com.saferent1.service;

import com.saferent1.domain.Role;
import com.saferent1.domain.User;
import com.saferent1.domain.enums.RoleType;
import com.saferent1.dto.request.RegisterRequest;
import com.saferent1.exception.ConflictException;
import com.saferent1.exception.ResourceNotFoundException;
import com.saferent1.exception.message.ErrorMessage;
import com.saferent1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserService {


    private UserRepository userRepository;

    private RoleService roleService;

    private PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, RoleService roleService,@Lazy PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
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
        user.setZipCode(user.getZipCode());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setRoles(roles);

        userRepository.save(user);


    }
}

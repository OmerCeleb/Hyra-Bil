package com.saferent1.service;

import com.saferent1.domain.User;
import com.saferent1.exception.ResourceNotFoundException;
import com.saferent1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User getUserByEmail(String email){
        userRepository.findByEmail(email).orElseThrow(
                ()-> new ResourceNotFoundException())
    }


}

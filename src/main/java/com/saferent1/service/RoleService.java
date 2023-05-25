package com.saferent1.service;

import com.saferent1.domain.Role;
import com.saferent1.domain.enums.RoleType;
import com.saferent1.exception.ResourceNotFoundException;
import com.saferent1.exception.message.ErrorMessage;
import com.saferent1.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    public Role findByType(RoleType roleType) {
        Role role = roleRepository.findByType(roleType).orElseThrow(() ->
                new ResourceNotFoundException(
                        String.format(ErrorMessage.ROLE_NOT_FOUND_EXCEPTION, roleType.name())));
        return role;

    }
}

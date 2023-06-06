package com.saferent1.dto;

import com.saferent1.domain.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor


@Table(name = "t_user")
public class UserDTO {


    private String firstName;

    private String lastName;

    private String email;

    private String password;

    private String phoneNumber;

    private String address;

    private Boolean builtIn;

    private String zipCode;


    private Set<String> roles;

    public void setRoles(Set<Role> roles) {
        Set<String> roleStr = new HashSet<>();
        roles.forEach(role -> {
            roleStr.add(role.getType().getName()); // Kund Administratör
        });
        this.roles = roleStr;
    }

}

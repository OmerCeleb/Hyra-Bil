package com.saferent1.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ContactMessageDTO {

    // Eftersom det är DTO lägger vi inte in någon anteckning relaterad till DB
    // Vi lägger alla fält eftersom vi ska göra mapping man jag ska inte sätta id

    private Long id;
    private String name;
    private String subject;
    private String body;
    private String email;
}

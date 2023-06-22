package com.saferent1.dto.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class UserUpdateRequest {


    @Size(max = 50)
    @NotBlank(message = "Ange ditt förnamn")
    // @NotBlank = charSequence.toString().trim().length()>0 inkluderar nutnull i notblank
    private String firstName;

    @Size(max = 50)
    @NotBlank(message = "Ange ditt efternamn")
    private String lastName;

    @Size(min = 5, max = 80)
    @Email(message = "Ange ditt e-mail")
    private String email;


    @Pattern(regexp = "^((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$", //(541) 317-8828
            message = "Ange ett giltigt telefonnummer")
    @Size(min = 14, max = 14)
    @NotBlank(message = "Ange ditt telefonnumret")
    private String phoneNumber;

    @Size(max = 100)
    @NotBlank(message = "Ange ditt adress")
    private String address;

    @Size(max = 15)
    @NotBlank(message = "Ange ditt postnummer")
    private String zipCode;


}

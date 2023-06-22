package com.saferent1.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor


public class UpdatePasswordRequest {

    @NotBlank(message = "Vänligen ange gammalt lösenord.")
    private String oldPassword;

    @NotBlank(message = "Vänligen ange nyt lösenord.")
    private String newPassword;


}

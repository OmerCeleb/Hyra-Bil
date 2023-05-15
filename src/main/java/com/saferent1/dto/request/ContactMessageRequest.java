package com.saferent1.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ContactMessageRequest {

    //Det finns inget id här eftersom id kommer att genereras automatiskt.
    //Vi kan inte ange id manuellt för att skapa detta Dto-meddelande. Spring kommer att tilldela sitt eget id.

    @Size(min = 2, max = 50, message = "Ditt namn '${validatedValue}' måste vara mellan {min} och {max} tecken långt.")
    @NotBlank(message = "Ange ditt namn.")
    private String name;

    @Size(min = 5, max = 50, message = "Ditt ämne '${validatedValue}' måste vara mellan {min} och {max} tecken långt.")
    @NotBlank(message = "Änge ditt ämne.")
    private String subject;

    @Size(min = 20, max = 200, message = "Din brödtext '${validatedValue}' måste vara mellan {min} och {max} tecken lång")
    @NotBlank(message = "Änge din brödtext.")
    private String body;

    @Email(message = "Änge ditt email.")
    private String email;
}

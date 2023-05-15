package com.saferent1.domain;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;




@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "tbl_cmessage")
public class ContactMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //ID kommer att genereras automatiskt
    @Setter(AccessLevel.NONE) //och inget inställt
    private Long id;

    @Size(min = 2, max = 50, message = "Ditt namn '${validatedValue}' måste vara mellan {min} och {max} tecken långt.")
    @NotNull(message = "Ange ditt namn.")
    @Column(length = 50, nullable = false)
    //Vi har redan kontrollerat båda fallen ovan, men vi skrev detta för att dubbelkolla
    private String name;

    @Size(min = 5, max = 50, message = "Ditt ämne '${validatedValue}' måste vara mellan {min} och {max} tecken långt.")
    @NotNull(message = "Änge ditt ämne.")
    @Column(length = 50, nullable = false)
    private String subject;

    @Size(min = 20, max = 200, message = "Din brödtext '${validatedValue}' måste vara mellan {min} och {max} tecken lång")
    @NotNull(message = "Änge din brödtext.")
    @Column(length = 200, nullable = false)
    private String body;

    @Email(message = "Änge ditt email.")
    @Column(length = 50, nullable = false)
    private String email;

}

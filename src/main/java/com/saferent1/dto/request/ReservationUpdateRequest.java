package com.saferent1.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.saferent1.domain.enums.ReservationStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor


public class ReservationUpdateRequest {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy HH:mm:ss")
    @NotNull(message = "Vänligen ange hämtningstiden för bokningen")
    private LocalDateTime pickUpTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy HH:mm:ss")
    @NotNull(message = "Vänligen ange droppe för bokningen")
    private LocalDateTime dropOfTime;

    @Size(max = 150, message = "Upphämtningsplats får vara max 150 tecken")
    @NotBlank(message = "Vänligen ange upphämtningsplats")
    private String pickUpLocation;

    @Size(max = 150, message = "Upphämtningsplats får vara max 150 tecken")
    @NotBlank(message = "Vänligen ange droppplats")
    private String dropOfLocation;

    private ReservationStatus status;
}

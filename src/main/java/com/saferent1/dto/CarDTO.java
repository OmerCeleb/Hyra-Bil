package com.saferent1.dto;

import com.saferent1.domain.ImageFile;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor


public class CarDTO {


    private Long id;

    @Size(max = 30, message = "Storleken överskrids")
    @NotBlank(message = "Ange bilmodell")
    private String model;

    @NotNull(message = "Vänligen ange information om bildörrar")
    private Integer doors;

    @NotNull(message = "Vänligen ange information om bilstolar")
    private Integer seats;

    @NotNull(message = "Vänligen ange information om bilens bagage")
    private Integer luggage;

    @NotBlank(message = "Vänligen ange information om bilöverföring")
    private String transmission;

    @NotNull(message = "Vänligen ange om bils luftkonditioneringsinformation")
    private Boolean airCondition;

    @NotNull(message = "Vänligen ange information om bilens ålder")
    private Integer age;

    @NotNull(message = "Vänligen ange bil per timme pris")
    private Double pricePerHour;

    @Size(max = 30, message = "Storleken överskrids")
    @NotBlank(message = "Vänligen ange bilbränsletyp")
    private String fuel;

    private Boolean builtIn = false;

    private Set<String> image;

}

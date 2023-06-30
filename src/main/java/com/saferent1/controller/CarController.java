package com.saferent1.controller;

import com.saferent1.mapper.CarMapper;
import com.saferent1.service.CarService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CarController {

    private final CarService carService;


    public CarController(CarService carService) {
        this.carService = carService;

    }
}

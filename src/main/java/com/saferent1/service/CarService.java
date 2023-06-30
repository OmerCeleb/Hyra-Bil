package com.saferent1.service;

import com.saferent1.mapper.CarMapper;
import org.springframework.stereotype.Service;

@Service
public class CarService {

    private final CarMapper carMapper;

    public CarService(CarMapper carMapper) {
        this.carMapper = carMapper;
    }


}

package com.saferent1.controller;

import com.saferent1.domain.Car;
import com.saferent1.dto.CarDTO;
import com.saferent1.dto.response.ResponseMessage;
import com.saferent1.dto.response.SfResponse;
import com.saferent1.mapper.CarMapper;
import com.saferent1.service.CarService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/car")
public class CarController {

    private final CarService carService;


    public CarController(CarService carService) {
        this.carService = carService;

    }

    //****************************************************************************'
    //!!SaveCar
    @PostMapping("/admin/{imageId}/add")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SfResponse> saveCar(
            @PathVariable String imageId, @Valid @RequestBody CarDTO carDTO) {
        carService.saveCar(imageId, carDTO);

        SfResponse response = new SfResponse(ResponseMessage.CAR_SAVED_RESPONSE_MESSAGE, true);

        return new ResponseEntity<>(response, HttpStatus.CREATED);

    }


    //****************************************************************************'
    // getAllCar
    @GetMapping("/visitors/all")
    public ResponseEntity<List<CarDTO>> getAllCar() {
        List<CarDTO> allCars = carService.getAllCars();

        return ResponseEntity.ok(allCars);
    }


}

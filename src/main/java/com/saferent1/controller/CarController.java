package com.saferent1.controller;

import com.saferent1.domain.Car;
import com.saferent1.dto.CarDTO;
import com.saferent1.dto.response.ResponseMessage;
import com.saferent1.dto.response.SfResponse;
import com.saferent1.mapper.CarMapper;
import com.saferent1.service.CarService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    //****************************************************************************'
    // !!! getAllWithPage
    @GetMapping("/visitors/pages")
    public ResponseEntity<Page<CarDTO>> getAllWithPage(
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @RequestParam("sort") String prop,
            @RequestParam(value = "direction",
                    required = false, defaultValue = "DESC") Sort.Direction direction
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, prop));

        Page<CarDTO> carDTOPage = carService.findAllWithPage(pageable);

        return ResponseEntity.ok(carDTOPage);

    }

    //****************************************************************************'
    // !!! getCarById
    @GetMapping("/visitors/{id}")
    public ResponseEntity<CarDTO> getCarById(@PathVariable Long id) {

        CarDTO carDTO = carService.findById(id);
        return ResponseEntity.ok(carDTO);

    }

    //****************************************************************************'
    // updateCarWithImageId
    @PutMapping("/admin/auth")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SfResponse> updateCar(
            @RequestParam("id") Long id,
            @RequestParam("imageId") String imageId,
            @Valid @RequestParam CarDTO carDTO) {


        carService.updateCar(id, imageId, carDTO);

        SfResponse response = new SfResponse(ResponseMessage.CAR_UPDATE_RESPONSE_MESSAGE, true);

        return ResponseEntity.ok(response);

    }

    //****************************************************************************'
    //Delete
    @DeleteMapping("/admin/{id}/auth")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SfResponse> deleteCar(@PathVariable Long id) {

        carService.removeById(id);

        SfResponse response = new SfResponse(ResponseMessage.CAR_DELETED_RESPONSE_MESSAGE, true);

        return ResponseEntity.ok(response);

    }


}

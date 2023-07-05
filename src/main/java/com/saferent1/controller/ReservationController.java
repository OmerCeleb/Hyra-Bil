package com.saferent1.controller;

import com.saferent1.domain.Car;
import com.saferent1.domain.User;
import com.saferent1.dto.request.ReservationRequest;
import com.saferent1.dto.response.ResponseMessage;
import com.saferent1.dto.response.SfResponse;
import com.saferent1.service.CarService;
import com.saferent1.service.ReservationService;
import com.saferent1.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/reservation")
public class ReservationController {

    private final ReservationService reservationService;
    private final CarService carService;

    private final UserService userService;

    public ReservationController(ReservationService reservationService, CarService carService, UserService userService) {
        this.reservationService = reservationService;
        this.carService = carService;
        this.userService = userService;
    }


    //****************************************************************************************'''''''
    //makeReservation
    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN') or hasRole('CUSTOMER')")
    public ResponseEntity<SfResponse> makeReservation(
            @RequestParam("carId") Long carId, @Valid @RequestBody ReservationRequest reservationRequest) {

        Car car = carService.getCarById(carId);
        User user = userService.getCurrentUser();


        reservationService.createReservation(reservationRequest, user, car);

        SfResponse response =
                new SfResponse(ResponseMessage.RESERVATION_CREATED_RESPONSE_MESSAGE, true);

        return new ResponseEntity<>(HttpStatus.CREATED);

    }

    //****************************************************************************************'''''''
    //adminMakeReservation
    @PostMapping("/add/auth")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SfResponse> addReservation(
            @RequestParam("carId") Long carId,
            @Valid @RequestBody ReservationRequest reservationRequest,
            @RequestParam("userId") Long userId) {

        Car car = carService.getCarById(carId);
        User user = userService.getById(userId);

        reservationService.createReservation(reservationRequest, user, car);

        SfResponse response = new SfResponse(
                ResponseMessage.RESERVATION_CREATED_RESPONSE_MESSAGE, true);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


}

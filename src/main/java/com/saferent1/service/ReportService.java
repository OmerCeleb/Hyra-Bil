package com.saferent1.service;

import org.springframework.stereotype.Service;

@Service
public class ReportService {

    private final UserService userService;
    private final CarService carService;
    private final ReservationService reservationService;

    public ReportService(UserService userService, CarService carService, ReservationService reservationService) {
        this.userService = userService;
        this.carService = carService;
        this.reservationService = reservationService;
    }



}

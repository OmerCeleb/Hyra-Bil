package com.saferent1.controller;

import com.saferent1.domain.Car;
import com.saferent1.domain.Reservation;
import com.saferent1.domain.User;
import com.saferent1.dto.CarDTO;
import com.saferent1.dto.ReservationDTO;
import com.saferent1.dto.request.ReservationRequest;
import com.saferent1.dto.request.ReservationUpdateRequest;
import com.saferent1.dto.response.CarAvailabilityResponse;
import com.saferent1.dto.response.ResponseMessage;
import com.saferent1.dto.response.SfResponse;
import com.saferent1.service.CarService;
import com.saferent1.service.ReservationService;
import com.saferent1.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/reservations")
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

    //****************************************************************************************'''''''
    //getAllReservations
    @GetMapping("/admin/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ReservationDTO>> getAllReservations() {

        List<ReservationDTO> reservations = reservationService.getAllReservations();

        return ResponseEntity.ok(reservations);


    }

    //****************************************************************************************'''''''
    //getAllReservationsWithPage
    @GetMapping("admin/all/pages")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<ReservationDTO>> getAllReservationsWithPage(
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @RequestParam("sort") String prop,
            @RequestParam(value = "direction",
                    required = false,
                    defaultValue = "DESC") Sort.Direction direction
    ) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, prop));

        Page<ReservationDTO> allReservations = reservationService.getAllWithPage(pageable);

        return ResponseEntity.ok(allReservations);

    }

    //****************************************************************************************'''''''
    //checkCarIsAvailable
    @GetMapping("/auth")
    @PreAuthorize("hasRole('ADMIN') or hasRole('CUSTOMER')")
    public ResponseEntity<SfResponse> checkCarIsAvailable(
            @RequestParam("carId") Long carId,
            @RequestParam("pickUpDateTime")
            @DateTimeFormat(pattern = "MM/dd/yyyy HH:mm:ss") LocalDateTime pickUpDateTime,
            @RequestParam("dropOffDateTime")
            @DateTimeFormat(pattern = "MM/dd/yyyy HH:mm:ss") LocalDateTime dropOffDateTime) {

        Car car = carService.getCarById(carId);

        boolean isAvailable = reservationService.checkCarAvailability(car, pickUpDateTime, dropOffDateTime);

        Double totalPrice = reservationService.getTotalPrice(car, pickUpDateTime, dropOffDateTime);

        SfResponse response =
                new CarAvailabilityResponse(ResponseMessage.CAR_AVAILABLE_MESSAGE, true, isAvailable, totalPrice);


        return ResponseEntity.ok(response);


    }

    //****************************************************************************************'''''''
    //update :(
    @PutMapping("admin/auth")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SfResponse> updateReservation(
            @RequestParam("carId") Long carId,
            @RequestParam("reservationId") Long reservationId,
            @Valid @RequestBody ReservationUpdateRequest reservationUpdateRequest) {

        Car car = carService.getCarById(carId);

        reservationService.updateReservation(reservationId, car, reservationUpdateRequest);

        SfResponse response = new SfResponse(ResponseMessage.RESERVATION_UPDATED_RESPONSE_MESSAGE, true);


        return ResponseEntity.ok(response);

    }

    //****************************************************************************************'''''''
    //!!! getReservationById - ADMIN
    @GetMapping("/{id}/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ReservationDTO> getReservationById(@PathVariable Long id) {

        ReservationDTO reservationDTO = reservationService.getReservationsDTO(id);

        return ResponseEntity.ok(reservationDTO);

    }

    //****************************************************************************************'''''''
    //!!! getReservationForSpecificUser - ADMIN
    @GetMapping("/admin/auth/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<ReservationDTO>> getAllUserReservations(
            @RequestParam("userId") Long userId,
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @RequestParam("sort") String prop,
            @RequestParam(value = "direction",
                    required = false,
                    defaultValue = "DESC") Sort.Direction direction
    ) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, prop));

        User user = userService.getById(userId);

        Page<ReservationDTO> reservationDTOS = reservationService.findReservationPageByuser(user, pageable);

        return ResponseEntity.ok(reservationDTOS);
    }

    //****************************************************************************************'''''''
    //!!! Kund eller administratör bör ta med sin egen bokningsinformation
    @GetMapping("/{id}/auth")
    @PreAuthorize("hasRole('ADMIN') or hasRole('CUSTOMER')")
    public ResponseEntity<ReservationDTO> getUserReservationById(@PathVariable Long id) {

        User user = userService.getCurrentUser();
        ReservationDTO reservationDTO = reservationService.findByIdAndUser(id, user);

        return ResponseEntity.ok(reservationDTO);
    }

    //****************************************************************************************'''''''
    //!!! getAllReservations
    @GetMapping("/auth/all")
    @PreAuthorize("hasRole('ADMIN') or hasRole('CUSTOMER')")
    public ResponseEntity<Page<ReservationDTO>> getAllUserReservations(
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @RequestParam("sort") String prop,
            @RequestParam(value = "direction",
                    required = false,
                    defaultValue = "DESC") Sort.Direction direction
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, prop));

        User user = userService.getCurrentUser();

        Page<ReservationDTO> reservationDTOS =
                reservationService.findReservationPageByuser(user, pageable);

        return ResponseEntity.ok(reservationDTOS);

    }

    //****************************************************************************************'''''''
    //deleteReservation
    @DeleteMapping("/admin/{id}/auth")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SfResponse> deleteReservation(@PathVariable Long id) {

        reservationService.removeById(id);

        SfResponse response =
                new SfResponse(ResponseMessage.RESERVATION_DELETED_RESPONSE_MESSAGE, true);

        return ResponseEntity.ok(response);

    }



}

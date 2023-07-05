package com.saferent1.service;

import com.saferent1.domain.Car;
import com.saferent1.domain.Reservation;
import com.saferent1.domain.User;
import com.saferent1.domain.enums.ReservationStatus;
import com.saferent1.dto.request.ReservationRequest;
import com.saferent1.exception.BadRequestException;
import com.saferent1.exception.message.ErrorMessage;
import com.saferent1.mapper.ReservationMapper;
import com.saferent1.repository.ReservationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class ReservationService {


    private final ReservationRepository reservationRepository;
    private final ReservationMapper reservationMapper;

    public ReservationService(ReservationRepository reservationRepository, ReservationMapper reservationMapper) {
        this.reservationRepository = reservationRepository;
        this.reservationMapper = reservationMapper;
    }

    public void createReservation(ReservationRequest reservationRequest, User user, Car car) {

        checkReservationTimeIsCorrect(reservationRequest.getPickUpTime(), reservationRequest.getDropOfTime());

        boolean carStatus = checkCarAvailability(car, reservationRequest.getPickUpTime(), reservationRequest.getDropOfTime());

        Reservation reservation = reservationMapper.reservationRequestToReservetion(reservationRequest);


        if (carStatus) {
            reservation.setStatus(ReservationStatus.CREATED);
        } else {
            throw new BadRequestException(ErrorMessage.CAR_NOT_AVAILABLE_MESSAGE);
        }

        reservation.setCar(car);
        reservation.setUser(user);

        Double totalPrice = getTotalPrice(car, reservationRequest.getPickUpTime(), reservationRequest.getDropOfTime());

        reservation.setTotalPrice(totalPrice);

        reservationRepository.save(reservation);

    }

    // !!! Är de begärda bokningsdatumen korrekta?
    private void checkReservationTimeIsCorrect(LocalDateTime pickUpTime, LocalDateTime dropOfTime) {

        LocalDateTime now = LocalDateTime.now();

        if (pickUpTime.isBefore(now)) {
            throw new BadRequestException(ErrorMessage.RESERVATION_TIME_INCORRECT_MESSAGE);
        }
        //!!! Är startdatum och slutdatum lika?
        boolean isEqual = pickUpTime.isEqual(dropOfTime) ? true : false;

        //!!! Är startdatum före slutdatumet?
        boolean isBefore = pickUpTime.isBefore(dropOfTime) ? true : false;

        if (isEqual || !isBefore) {
            throw new BadRequestException(ErrorMessage.RESERVATION_TIME_INCORRECT_MESSAGE);

        }

    }

    //!!! Är fordonet tillgängligt?
    private boolean checkCarAvailability(Car car, LocalDateTime pickUpTime,
                                         LocalDateTime dropOfTime) {

        List<Reservation> existReservations = getConflictReservation(car, pickUpTime, dropOfTime);

        return existReservations.isEmpty();

    }

    // !!! prisberäkning
    private Double getTotalPrice(Car car, LocalDateTime pickUpTime,
                                 LocalDateTime dropOfTime) {
        Long minutes = ChronoUnit.MINUTES.between(pickUpTime, dropOfTime);
        double hours = Math.ceil(minutes / 60.0);

        return car.getPricePerHour() * hours;

    }

    // !!! connflict?
    private List<Reservation> getConflictReservation(Car car, LocalDateTime pickUpTime,
                                                     LocalDateTime dropOfTime) {

        if (pickUpTime.isAfter(dropOfTime)) {
            throw new BadRequestException(ErrorMessage.RESERVATION_TIME_INCORRECT_MESSAGE);
        }

        ReservationStatus[] status = {ReservationStatus.CANCELED, ReservationStatus.DONE};

        List<Reservation> existReservation =
                reservationRepository.checkCarStatus(car.getId(), pickUpTime, dropOfTime, status);

        return existReservation;

    }


}

package com.saferent1.service;

import com.saferent1.domain.Car;
import com.saferent1.domain.Reservation;
import com.saferent1.domain.User;
import com.saferent1.domain.enums.ReservationStatus;
import com.saferent1.dto.request.ReservationRequest;
import com.saferent1.exception.BadRequestException;
import com.saferent1.exception.message.ErrorMessage;
import com.saferent1.repository.ReservationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReservationService {


    private final ReservationRepository reservationRepository;

    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public void createReservation(ReservationRequest reservationRequest, User user, Car car) {


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


    // !!! prisberäkning

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

package com.saferent1.service;

import com.saferent1.domain.Car;
import com.saferent1.domain.Reservation;
import com.saferent1.domain.User;
import com.saferent1.domain.enums.ReservationStatus;
import com.saferent1.dto.ReservationDTO;
import com.saferent1.dto.request.ReservationRequest;
import com.saferent1.dto.request.ReservationUpdateRequest;
import com.saferent1.exception.BadRequestException;
import com.saferent1.exception.ResourceNotFoundException;
import com.saferent1.exception.message.ErrorMessage;
import com.saferent1.mapper.ReservationMapper;
import com.saferent1.repository.ReservationRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public void checkReservationTimeIsCorrect(LocalDateTime pickUpTime, LocalDateTime dropOfTime) {

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
    public boolean checkCarAvailability(Car car, LocalDateTime pickUpTime,
                                        LocalDateTime dropOfTime) {

        List<Reservation> existReservations = getConflictReservation(car, pickUpTime, dropOfTime);

        return existReservations.isEmpty();

    }

    // !!! prisberäkning
    public Double getTotalPrice(Car car, LocalDateTime pickUpTime,
                                LocalDateTime dropOfTime) {
        Long minutes = ChronoUnit.MINUTES.between(pickUpTime, dropOfTime);
        double hours = Math.ceil(minutes / 60.0);

        return car.getPricePerHour() * hours;

    }

    // !!! connflict?
    public List<Reservation> getConflictReservation(Car car, LocalDateTime pickUpTime,
                                                    LocalDateTime dropOfTime) {

        if (pickUpTime.isAfter(dropOfTime)) {
            throw new BadRequestException(ErrorMessage.RESERVATION_TIME_INCORRECT_MESSAGE);
        }

        ReservationStatus[] status = {ReservationStatus.CANCELED, ReservationStatus.DONE};

        List<Reservation> existReservation =
                reservationRepository.checkCarStatus(car.getId(), pickUpTime, dropOfTime, status);

        return existReservation;

    }


    public List<ReservationDTO> getAllReservations() {

        List<Reservation> reservations = reservationRepository.findAll();
        return reservationMapper.map(reservations);

    }

    public Page<ReservationDTO> getAllWithPage(Pageable pageable) {

        Page<Reservation> reservationPage = reservationRepository.findAll(pageable);

        return reservationPage.map(reservationMapper::reservationToReservationDTO);

    }

    public void updateReservation(Long reservationId, Car car, ReservationUpdateRequest reservationUpdateRequest) {

        Reservation reservation = reservationRepository.getById(reservationId);

        //Om bokningsstatusen är annullerad eller klar kan du inte uppdatera.
        if (reservation.getStatus().equals(ReservationStatus.CANCELED) ||
                reservation.getStatus().equals(ReservationStatus.DONE)) {
            throw new BadRequestException(ErrorMessage.RESERVATION_STATUS_CANT_CHANGE_MESSAGE);
        }

        // !!!Om reservationen kommer att uppdateras och statusen inte kommer att uppdateras,
        // bör pickUpTime och dropOfTime inte kontrolleras.
        if (reservationUpdateRequest.getStatus() != null &&
                reservationUpdateRequest.getStatus() == ReservationStatus.CREATED) {

            checkReservationTimeIsCorrect(reservationUpdateRequest.getPickUpTime(),
                    reservationUpdateRequest.getDropOfTime());

            //!!! Conflict kontroll
            List<Reservation> conflictReservations = getConflictReservation(
                    car, reservationUpdateRequest.getPickUpTime(),
                    reservationUpdateRequest.getDropOfTime());


            if (!conflictReservations.isEmpty()) {
                if (!(conflictReservations.size() == 1 &&
                        conflictReservations.get(0).getId().equals(reservationId))) {
                    throw new BadRequestException((ErrorMessage.CAR_NOT_AVAILABLE_MESSAGE));
                }
            }

            // pris beräknad
            Double totalPrice = getTotalPrice(car, reservationUpdateRequest.getPickUpTime(), reservationUpdateRequest.getDropOfTime());

            reservation.setTotalPrice(totalPrice);
            reservation.setCar(car);
        }

        reservation.setPickUpTime(reservationUpdateRequest.getPickUpTime());
        reservation.setDropOfTime(reservationUpdateRequest.getDropOfTime());
        reservation.setPickUpLocation(reservationUpdateRequest.getPickUpLocation());
        reservation.setDropOfTime(reservationUpdateRequest.getDropOfTime());
        reservation.setStatus(reservationUpdateRequest.getStatus());

        reservationRepository.save(reservation);

    }

    public Reservation getById(Long id) {

        Reservation reservation = reservationRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(String.format(ErrorMessage.RESOURCE_NOT_FOUND_EXCEPTION, id)));


        return reservation;
    }

    public ReservationDTO getReservationsDTO(Long id) {

        Reservation reservation = getById(id);

        return reservationMapper.reservationToReservationDTO(reservation);

    }

    public Page<ReservationDTO> findReservationPageByuser(User user, Pageable pageable) {

        Page<Reservation> reservationPage = reservationRepository.findAllByUser(user, pageable);

        return reservationPage.map(reservationMapper::reservationToReservationDTO);
    }
}

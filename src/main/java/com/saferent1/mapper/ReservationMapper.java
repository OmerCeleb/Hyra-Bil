package com.saferent1.mapper;

import com.saferent1.domain.Reservation;
import com.saferent1.dto.ReservationDTO;
import com.saferent1.dto.request.ReservationRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReservationMapper {


    Reservation reservationRequestToReservetion(ReservationRequest reservationRequest);


}

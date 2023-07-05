package com.saferent1.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CarAvailabilityResponse extends SfResponse {

    private boolean available;

    private double totalPrice;

    public CarAvailabilityResponse(String message, boolean succes, boolean available, double totalPrice){
        super(message,succes);
        this.available = available;
        this.totalPrice = totalPrice;
    }

}

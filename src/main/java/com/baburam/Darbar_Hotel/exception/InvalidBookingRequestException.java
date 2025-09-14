package com.baburam.Darbar_Hotel.exception;

public class InvalidBookingRequestException extends RuntimeException {

    public InvalidBookingRequestException(String message){
        super(message);
    }
}

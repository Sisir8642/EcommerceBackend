package com.baburam.Darbar_Hotel.service;

import com.baburam.Darbar_Hotel.model.BookedRoom;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IBookingService {
    void cancelBooking(Long bookingId);

    String saveBooking(Long roomId, BookedRoom bookingRequest);

    BookedRoom findByBookingConfirmationCode(String confirmationCode);

    List<BookedRoom> getAllBookings();
}
 
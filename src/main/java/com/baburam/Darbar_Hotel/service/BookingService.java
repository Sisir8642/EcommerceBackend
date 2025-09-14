package com.baburam.Darbar_Hotel.service;


import com.baburam.Darbar_Hotel.exception.InvalidBookingRequestException;
import com.baburam.Darbar_Hotel.model.BookedRoom;
import com.baburam.Darbar_Hotel.model.Room;
import com.baburam.Darbar_Hotel.repository.BookingRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingService implements IBookingService{

    private final BookingRepository bookingRepository;
    private final IRoomService roomService;

    public BookingService(BookingRepository bookingRepository, IRoomService roomService) {
        this.bookingRepository = bookingRepository;
        this.roomService = roomService;
    }

    @Override
    public List<BookedRoom> getAllBookings() {
        return bookingRepository.findAll();
    }



    @Override
    public void cancelBooking(Long bookingId) {
    bookingRepository.deleteById(bookingId);

    }

    public List<BookedRoom> getAllBookingsByRoomId(Long roomId) {
        return bookingRepository.findByRoomId(roomId);
    }

    @Override
    public String saveBooking(Long roomId, BookedRoom bookingRequest) {
        if (!bookingRequest.getCheckInDate().isBefore(bookingRequest.getCheckOutDate())){
            throw new InvalidBookingRequestException("Check-in date must before check-out data ");
        }
        Room room = roomService.getRoomById(roomId).get();
        List<BookedRoom> existingBookings = room.getBookings();
        boolean roomIsAvailable = roomIsAvailable(bookingRequest,existingBookings);
        if (roomIsAvailable){
            room.addBooking(bookingRequest);
            bookingRepository.save(bookingRequest);
        }else {
            throw new InvalidBookingRequestException("sorry, This room is not available for the selected dates;");
        }
        return bookingRequest.getBookingConfirmationCode();
    }

    @Override
    public BookedRoom findByBookingConfirmationCode(String confirmationCode) {
        return bookingRepository.findByBookingConfirmationCode(confirmationCode);
    }

    private boolean roomIsAvailable(BookedRoom bookingRequest, List<BookedRoom> existingBookings) {
        return existingBookings.stream()
                .noneMatch(existingBooking ->
                                bookingRequest.getCheckInDate().equals(existingBooking.getCheckOutDate())
                       || bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckOutDate())
                        || (bookingRequest.getCheckInDate().isAfter(existingBooking.getCheckInDate())
                        && bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckOutDate()))
                        || (bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckOutDate())

                        && bookingRequest.getCheckOutDate().equals(existingBooking.getCheckOutDate()))
                        || (bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckInDate())

                        && bookingRequest.getCheckOutDate().isAfter(existingBooking.getCheckOutDate()))

                        || (bookingRequest.getCheckInDate().equals(existingBooking.getCheckOutDate())
                        && bookingRequest.getCheckOutDate().equals(existingBooking.getCheckInDate()))

                        || (bookingRequest.getCheckInDate().equals(existingBooking.getCheckOutDate())
                        && bookingRequest.getCheckInDate().equals(existingBooking.getCheckInDate()))
                );
     }

}

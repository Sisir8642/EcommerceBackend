package com.baburam.Darbar_Hotel.model;

import jakarta.persistence.*;
import org.apache.commons.lang3.RandomStringUtils;

import java.math.BigDecimal;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String roomType;
    private BigDecimal roomPrice;
    private boolean isBooked = false;

    @Lob
    private Blob photo;

    @OneToMany(mappedBy = "room", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<BookedRoom> bookings = new ArrayList<>();

    // No-arg constructor
    public Room() {}

    // All-args constructor
    public Room(Long id, String roomType, BigDecimal roomPrice, boolean isBooked, Blob photo, List<BookedRoom> bookings) {
        this.id = id;
        this.roomType = roomType;
        this.roomPrice = roomPrice;
        this.isBooked = isBooked;
        this.photo = photo;
        this.bookings = bookings != null ? bookings : new ArrayList<>();
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getRoomType() {
        return roomType;
    }

    public BigDecimal getRoomPrice() {
        return roomPrice;
    }

    public boolean isBooked() {
        return isBooked;
    }

    public Blob getPhoto() {
        return photo;
    }

    public List<BookedRoom> getBookings() {
        return bookings;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public void setRoomPrice(BigDecimal roomPrice) {
        this.roomPrice = roomPrice;
    }

    public void setBooked(boolean booked) {
        isBooked = booked;
    }

    public void setPhoto(Blob photo) {
        this.photo = photo;
    }

    public void setBookings(List<BookedRoom> bookings) {
        this.bookings = bookings != null ? bookings : new ArrayList<>();
    }

    // Helper Method
    public void addBooking(BookedRoom booking) {
        if (this.bookings == null) {
            this.bookings = new ArrayList<>();
        }
        this.bookings.add(booking);
        booking.setRoom(this);
        this.isBooked = true;

        String bookingCode = RandomStringUtils.randomNumeric(10);
        booking.setBookingConfirmationCode(bookingCode);
    }
}

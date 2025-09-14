package com.baburam.Darbar_Hotel.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "booked_rooms")
public class BookedRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookingId;

    @Column(name = "check_in")
    private LocalDate checkInDate;

    @Column(name = "check_out")
    private LocalDate checkOutDate;

    @Column(name = "guest_full_name")
    private String guestFullName;

    @Column(name = "guest_email")
    private String guestEmail;

    @Column(name = "num_of_adults")
    private int numOfAdults;

    @Column(name = "num_of_children")
    private int numOfChildren;

    @Column(name = "total_num_of_guests")
    private int totalNumOfGuest;

    @Column(name = "confirmation_code")
    private String bookingConfirmationCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room;

    // ---------- Constructors ----------
    public BookedRoom() {}

    public BookedRoom(Long bookingId, LocalDate checkInDate, LocalDate checkOutDate, String guestFullName,
                      String guestEmail, int numOfAdults, int numOfChildren, String bookingConfirmationCode, Room room) {
        this.bookingId = bookingId;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.guestFullName = guestFullName;
        this.guestEmail = guestEmail;
        this.numOfAdults = numOfAdults;
        this.numOfChildren = numOfChildren;
        this.bookingConfirmationCode = bookingConfirmationCode;
        this.room = room;
        calculateTotalNumberOfGuests();
    }

    // ---------- Getters ----------
    public Long getBookingId() {
        return bookingId;
    }

    public LocalDate getCheckInDate() {
        return checkInDate;
    }

    public LocalDate getCheckOutDate() {
        return checkOutDate;
    }

    public String getGuestFullName() {
        return guestFullName;
    }

    public String getGuestEmail() {
        return guestEmail;
    }

    public int getNumOfAdults() {
        return numOfAdults;
    }

    public int getNumOfChildren() {
        return numOfChildren;
    }

    public int getTotalNumOfGuest() {
        return totalNumOfGuest;
    }

    public String getBookingConfirmationCode() {
        return bookingConfirmationCode;
    }

    public Room getRoom() {
        return room;
    }

    // ---------- Setters ----------
    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }

    public void setCheckInDate(LocalDate checkInDate) {
        this.checkInDate = checkInDate;
    }

    public void setCheckOutDate(LocalDate checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public void setGuestFullName(String guestFullName) {
        this.guestFullName = guestFullName;
    }

    public void setGuestEmail(String guestEmail) {
        this.guestEmail = guestEmail;
    }

    public void setNumOfAdults(int numOfAdults) {
        this.numOfAdults = numOfAdults;
        calculateTotalNumberOfGuests();
    }

    public void setNumOfChildren(int numOfChildren) {
        this.numOfChildren = numOfChildren;
        calculateTotalNumberOfGuests();
    }

    public void setTotalNumOfGuest(int totalNumOfGuest) {
        this.totalNumOfGuest = totalNumOfGuest;
    }

    public void setBookingConfirmationCode(String bookingConfirmationCode) {
        this.bookingConfirmationCode = bookingConfirmationCode;
    }

    public void setRoom(Room room) {
        this.room = room;
    }


    private void calculateTotalNumberOfGuests() {
        this.totalNumOfGuest = this.numOfAdults + this.numOfChildren;
    }
}

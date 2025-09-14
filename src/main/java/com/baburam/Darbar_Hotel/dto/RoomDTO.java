package com.baburam.Darbar_Hotel.dto;

import java.math.BigDecimal;
import java.sql.Blob;

public class RoomDTO {
    private Long id;
    private String roomType;
    private BigDecimal roomPrice;
    private boolean isBooked;



    public RoomDTO() {}

    public RoomDTO(Long id, String roomType, BigDecimal roomPrice, boolean isBooked) {
        this.id = id;
        this.roomType = roomType;
        this.roomPrice = roomPrice;
        this.isBooked = isBooked;
    }


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

}

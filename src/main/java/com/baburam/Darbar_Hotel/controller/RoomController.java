package com.baburam.Darbar_Hotel.controller;

import com.baburam.Darbar_Hotel.dto.RoomDTO;
import com.baburam.Darbar_Hotel.exception.PhotoRetrievalException;
import com.baburam.Darbar_Hotel.exception.ResourceNotFoundException;
import com.baburam.Darbar_Hotel.model.BookedRoom;
import com.baburam.Darbar_Hotel.model.Room;
import com.baburam.Darbar_Hotel.response.RoomResponse;
import com.baburam.Darbar_Hotel.service.BookingService;
import com.baburam.Darbar_Hotel.service.IRoomService;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "https://hotelbookingjava.netlify.app")
@RestController
@RequestMapping("/room")
@RequiredArgsConstructor
public class RoomController {

    private final IRoomService roomService;
    private final BookingService bookingService;

    // Add new room
    @PostMapping("/add/new-room")
    public ResponseEntity<RoomDTO> addNewRoom(
            @RequestParam("photo") MultipartFile photo,
            @RequestParam("roomType") String roomType,
            @RequestParam("roomPrice") BigDecimal roomPrice
    ) throws SQLException, IOException {

        Room savedRoom = roomService.addNewRoom(photo, roomType, roomPrice);

        RoomDTO response = roomService.convertToDTO(savedRoom);
        return ResponseEntity.ok(response);
    }

    // Get all room types
    @GetMapping("/room-types")
    public ResponseEntity<List<String>> getRoomTypes() {
        List<String> types = roomService.getAllRoomTypes();
        return ResponseEntity.ok(types);
    }

    // Get all rooms with Base64 photos
    @GetMapping("/all-rooms")
    public ResponseEntity<List<RoomResponse>> getAllRooms() throws SQLException {
        List<Room> rooms = roomService.getALlRooms();
        List<RoomResponse> responses = new ArrayList<>();

        for (Room room : rooms) {
            byte[] photoBytes = roomService.getRoomPhotoByRoomId(room.getId());
            String base64Photo = null;
            if (photoBytes != null && photoBytes.length > 0) {
                base64Photo = Base64.encodeBase64String(photoBytes);
            }
            RoomResponse response = new RoomResponse(
                    room.getId(),
                    room.getRoomType(),
                    room.getRoomPrice(),
                    room.isBooked(),
                    base64Photo != null ? base64Photo.getBytes() : null
            );
            responses.add(response);
        }

        return ResponseEntity.ok(responses);
    }

    // Get room by ID
    @GetMapping("/{roomId}")
    public ResponseEntity<RoomResponse> getRoomById(@PathVariable("roomId") Long roomId) throws SQLException {
        Room room = roomService.getRoomById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found"));

        byte[] photoBytes = roomService.getRoomPhotoByRoomId(roomId);
        RoomResponse response = new RoomResponse(
                room.getId(),
                room.getRoomType(),
                room.getRoomPrice(),
                room.isBooked(),
                photoBytes
        );

        return ResponseEntity.ok(response);
    }

    // Update room
    @PutMapping("/update/{roomId}")
    public ResponseEntity<RoomResponse> updateRoom(
            @PathVariable("roomId") Long roomId,
            @RequestParam(required = false) String roomType,
            @RequestParam(required = false) BigDecimal roomPrice,
            @RequestParam(required = false) MultipartFile photo
    ) throws IOException, SQLException {

        byte[] photoBytes = null;
        if (photo != null && !photo.isEmpty()) {
            photoBytes = photo.getBytes();
        } else {
            photoBytes = roomService.getRoomPhotoByRoomId(roomId);
        }

        Room updatedRoom = roomService.updateRoom(roomId, roomType, roomPrice, photoBytes);
        updatedRoom.setPhoto(photoBytes != null ? new SerialBlob(photoBytes) : null);

        RoomResponse response = new RoomResponse(
                updatedRoom.getId(),
                updatedRoom.getRoomType(),
                updatedRoom.getRoomPrice(),
                updatedRoom.isBooked(),
                photoBytes
        );

        return ResponseEntity.ok(response);
    }

    // Delete room
    @DeleteMapping("/delete/{roomId}")
    public ResponseEntity<Void> deleteRoom(@PathVariable("roomId") Long roomId) {
        roomService.deleteRoom(roomId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}

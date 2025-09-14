package com.baburam.Darbar_Hotel.controller;

import com.baburam.Darbar_Hotel.dto.RoomDTO;
import com.baburam.Darbar_Hotel.exception.PhotoRetrievalException;
import com.baburam.Darbar_Hotel.exception.ResourceNotFoundException;
import com.baburam.Darbar_Hotel.model.BookedRoom;
import com.baburam.Darbar_Hotel.model.Room;
import com.baburam.Darbar_Hotel.response.BookingResponse;
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
@ResponseBody
@RequiredArgsConstructor
@RequestMapping("/room")
public class RoomController {
    private IRoomService roomService;
    private BookingService bookingService;

    public RoomController(IRoomService roomService, BookingService bookingService) {
        this.roomService = roomService;
        this.bookingService = bookingService;
    }

    @PostMapping("/add/new-room")
    public ResponseEntity<RoomDTO> addNewRoom(
          @RequestParam("photo") MultipartFile photo,
          @RequestParam("roomType")  String roomType,
          @RequestParam("roomPrice")  BigDecimal roomPrice) throws SQLException, IOException {

        System.out.println("Received roomType: " + roomType);
        System.out.println("Received roomPrice: " + roomPrice);
        System.out.println("Received file: " + photo.getOriginalFilename());

        Room savedRoom = roomService.addNewRoom(photo, roomType, roomPrice);

         RoomDTO response = new RoomDTO(
                 savedRoom.getId(),
                 savedRoom.getRoomType(),
                 savedRoom.getRoomPrice(),
                 savedRoom.isBooked()
         );

         return ResponseEntity.ok(response);

    }

    @GetMapping("/room-types")
    public List<String> getRoomTypes(){
         return roomService.getAllRoomTypes();
    }
    @GetMapping("/all-rooms")
public ResponseEntity<List<RoomResponse>> getAllRooms() throws SQLException {
         List<Room> rooms= roomService.getALlRooms();
         List<RoomResponse> roomResponses= new ArrayList<>();
         for (Room room : rooms){
             byte[] photoBytes = roomService.getRoomPhotoByRoomId(room.getId());
             if(photoBytes !=null && photoBytes.length>0){
                 String base64Photo = Base64.encodeBase64String(photoBytes);
                 RoomResponse roomResponse = getRoomResponse(room);
                 roomResponse .setPhoto(base64Photo);
                 roomResponses.add(roomResponse);
             }
         }
         return ResponseEntity.ok(roomResponses);
}


@DeleteMapping("/delete/room/{roomId}")
   public ResponseEntity<Void> deleteRoom(@PathVariable  Long roomId){
        roomService.deleteRoom(roomId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
   }

   @PutMapping("/update/{roomId}")
   public ResponseEntity<RoomResponse> updateRoom( @PathVariable Long roomId,
                                                 @RequestParam(required = false) String roomType,
                                                  @RequestParam(required = false) BigDecimal roomPrice,
                                                  @RequestParam(required = false) MultipartFile photo) throws IOException, SQLException {

   byte[] photoBytes = photo != null && !photo.isEmpty()?
           photo.getBytes(): roomService.getRoomPhotoByRoomId(roomId);

   Blob photoBlob = photoBytes!= null && photoBytes.length>0 ? new SerialBlob(photoBytes): null;
   Room theRoom = roomService.updateRoom(roomId, roomType,roomPrice, photoBytes);
   theRoom.setPhoto(photoBlob);
   RoomResponse roomResponse = getRoomResponse(theRoom);
  return ResponseEntity.ok(roomResponse);
   }

   @GetMapping("/room/{roomId}")
   public ResponseEntity<Optional<RoomResponse>> getRoomById(@PathVariable Long roomId){
        Optional<Room> theRoom = roomService.getRoomById(roomId);
        return theRoom.map(room -> {
            RoomResponse roomResponse = getRoomResponse(room);
            return ResponseEntity.ok(Optional.of(roomResponse));
        }).orElseThrow(() -> new ResourceNotFoundException("Room not found!!!"));
   }

    private RoomResponse getRoomResponse(Room room) {

         List<BookedRoom> bookings= getAllBookingsByRoomId(room.getId());
//         List<BookingResponse> bookingInfo = bookings
//                 .stream()
//                 .map(booking -> new BookingResponse( booking.getBookingId(),
//                         booking.getCheckInDate(),
//                         booking.getCheckOutDate(), booking.getBookingConfirmationCode())).toList();

                 byte[] photoBytes = null;
                 Blob photoBlob = room.getPhoto();
                 if(photoBlob != null){
                     try {
                         photoBytes = photoBlob.getBytes(1,(int) photoBlob.length());
                     } catch (SQLException e){
                         throw new PhotoRetrievalException("Error Retrieving photo");
                     }
                 }
                 return new RoomResponse(room.getId(),
                         room.getRoomType(),
                         room.getRoomPrice(),
                         room.isBooked(), photoBytes);
    }

    private List<BookedRoom> getAllBookingsByRoomId(Long roomId) {
         return bookingService.getAllBookingsByRoomId(roomId);
    }


}

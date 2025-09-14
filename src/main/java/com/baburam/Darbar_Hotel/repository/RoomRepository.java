package com.baburam.Darbar_Hotel.repository;

import com.baburam.Darbar_Hotel.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    @Query("SELECT r.roomType FROM Room r")
    List<String> findDistinctRoomTypes();
}

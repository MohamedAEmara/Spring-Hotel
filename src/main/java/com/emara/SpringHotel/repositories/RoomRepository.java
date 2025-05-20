package com.emara.SpringHotel.repositories;

import com.emara.SpringHotel.dto.ResponseDTO;
import com.emara.SpringHotel.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {
    @Query("SELECT DISTINCT r.roomType FROM Room r")
    ResponseDTO findDistinctRoomTypes();

    // Get Available Rooms  ==>  find rooms that are not in the booking table
    @Query("SELECT r FROM Room r WHERE r.id NOT IN (SELECT b.room.id FROM Booking b)")
    List<Room> findAvailableRooms();


    @Query("""
        SELECT r FROM Room r 
        WHERE r.roomType LIKE CONCAT('%', :roomType, '%') 
          AND r.id NOT IN (
            SELECT b.room.id FROM Booking b 
            WHERE b.checkInDate <= :checkOutDate 
              AND b.checkOutDate >= :checkInDate
        )
    """)
    List<Room> findAvailableRoomsByDateAndTypes(LocalDate checkInDate, LocalDate checkOutDate, String roomType);

}

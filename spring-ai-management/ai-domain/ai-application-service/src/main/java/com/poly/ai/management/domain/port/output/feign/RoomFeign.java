package com.poly.ai.management.domain.port.output.feign;

import com.poly.ai.management.domain.dto.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "room-service", url = "localhost:8087/api")
public interface RoomFeign {

    @GetMapping("/rooms")
    ResponseEntity<List<RoomResponse>> getAllRooms();

    @GetMapping("/rooms/types")
    ResponseEntity<List<RoomTypeResponse>> getAllRoomTypes();

    @GetMapping("/rooms/{roomNumber}")
    ResponseEntity<RoomResponse> getRoomByNumber(@PathVariable("roomNumber") String roomNumber);

    @GetMapping("/rooms/status/{status}")
    ResponseEntity<List<RoomResponse>> getRoomsByStatus(@PathVariable("status") String status);

    @GetMapping("/rooms/floor/{floor}")
    ResponseEntity<List<RoomResponse>> getRoomsByFloor(@PathVariable("floor") int floor);

    @GetMapping("/rooms/type/{typeName}")
    ResponseEntity<List<RoomResponse>> getRoomsByType(@PathVariable("typeName") String typeName);

    @GetMapping("/furniture")
    ResponseEntity<List<FurnitureRequirementResponse>> getAllFurniture();

    @GetMapping("/maintenance")
    ResponseEntity<List<RoomMaintenanceResponse>> getAllMaintenance();

    @GetMapping("/maintenance/room/{roomNumber}")
    ResponseEntity<List<RoomMaintenanceResponse>> getMaintenanceByRoom(@PathVariable("roomNumber") String roomNumber);

    @GetMapping("/cleaning")
    ResponseEntity<List<RoomCleaningResponse>> getAllCleaning();

    @GetMapping("/cleaning/room/{roomNumber}")
    ResponseEntity<List<RoomCleaningResponse>> getCleaningByRoom(@PathVariable("roomNumber") String roomNumber);

    @GetMapping("/services")
    ResponseEntity<List<RoomServiceResponse>> getAllServices();

    @GetMapping("/services/room/{roomNumber}")
    ResponseEntity<List<RoomServiceResponse>> getServicesByRoom(@PathVariable("roomNumber") String roomNumber);

    @GetMapping("/guests")
    ResponseEntity<List<GuestResponse>> getAllGuests(@RequestParam("page") int page, @RequestParam("size") int size);

    @GetMapping("/guests/search")
    ResponseEntity<List<GuestResponse>> searchGuests(@RequestParam("name") String name, 
                                                   @RequestParam("phone") String phone, 
                                                   @RequestParam("email") String email);

    @GetMapping("/rooms/statistics")
    ResponseEntity<Object> getRoomStatistics();

    @GetMapping("/rooms/count")
    ResponseEntity<Object> getRoomCount();

    @GetMapping("/rooms/count/available")
    ResponseEntity<Object> getAvailableRoomCount();

    @GetMapping("/rooms/count/occupied")
    ResponseEntity<Object> getOccupiedRoomCount();

    @GetMapping("/rooms/count/maintenance")
    ResponseEntity<Object> getMaintenanceRoomCount();

    @GetMapping("/rooms/occupancy-ratio")
    ResponseEntity<Object> getOccupancyRatio();
}

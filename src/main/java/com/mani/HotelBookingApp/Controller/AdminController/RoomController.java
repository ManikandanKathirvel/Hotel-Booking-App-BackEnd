package com.mani.HotelBookingApp.Controller.AdminController;//NOSONAR

import com.mani.HotelBookingApp.DTO.RoomDTO;
import com.mani.HotelBookingApp.Exceptions.ResourceNotFoundException;
import com.mani.HotelBookingApp.Service.Admin.Rooms.RoomService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("http://localhost:4200")
@RequestMapping("/api/admin")
public class RoomController {


    private final RoomService service;

    public RoomController(RoomService service) {
        this.service = service;
    }

    @PostMapping("/rooms")
    public ResponseEntity<?> postRooms(@RequestBody RoomDTO roomDTO) {
        boolean success = service.postRooms(roomDTO);
        if (success) {
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            throw new IllegalArgumentException("Invalid Room Data");
        }
    }

    @GetMapping("/rooms/{pageNumber}")
    public ResponseEntity<?> getAllRoom(@PathVariable int pageNumber) {
        if (pageNumber == -1) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Room Data");
        }
        return ResponseEntity.ok(service.getAllRooms(pageNumber));
    }

    @GetMapping("/room/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(service.getRoomById(id));
        } catch (EntityNotFoundException en) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/updateRoom/{id}")
    public ResponseEntity<?> updateRoom(@PathVariable Long id, @RequestBody RoomDTO roomDTO) throws ResourceNotFoundException {
        boolean success = service.updateRoom(id, roomDTO);
        if (success) {
            return ResponseEntity.status(HttpStatus.OK).body(true);
        }
        throw new ResourceNotFoundException("Room not found with ID: " + id);
    }

    @DeleteMapping("/deleteRoom/{id}")
    public ResponseEntity<?> deleteRoom(@PathVariable Long id) {
        try {
            service.deleteRoom(id);
            return ResponseEntity.ok("successfully deleted");
        } catch (EntityNotFoundException ev) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ev.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}

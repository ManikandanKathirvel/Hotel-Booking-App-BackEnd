package com.mani.HotelBookingApp.Controller.AdminController;//NOSONAR

import com.mani.HotelBookingApp.Service.Admin.Reservation.ReservationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin("http://localhost:4200")
public class ReservationController {
    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping("/reservation/{pageNo}")
    public ResponseEntity<?> getAllReservation(@PathVariable int pageNo) {
        try {
            return ResponseEntity.ok(reservationService.getALlReservation(pageNo));
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid status or reservation ID");
        }
    }

    @GetMapping("/res/{id}/{status}")
    public ResponseEntity<?> changeReservationStatus(@PathVariable Long id, @PathVariable String status) {
        boolean success = reservationService.changeReservationStatus(id, status);
        System.out.println(reservationService.changeReservationStatus(id,status));
        if (success) {
            return ResponseEntity.ok().build();
        } else {
            throw new IllegalArgumentException("Invalid status or reservation ID");
        }
    }
}

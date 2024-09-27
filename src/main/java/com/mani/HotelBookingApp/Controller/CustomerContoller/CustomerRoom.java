package com.mani.HotelBookingApp.Controller.CustomerContoller;//NOSONAR

import com.mani.HotelBookingApp.Exceptions.IllegalArgumentException;
import com.mani.HotelBookingApp.Service.Customer.Room.CustomerRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.print.DocFlavor;

@RestController
@RequestMapping("/api/customer")
@CrossOrigin("http://localhost:4200")
public class CustomerRoom {
    @Autowired
    CustomerRoomService service;

    @GetMapping("/getroom/{pageNumber}")
    public ResponseEntity<?> getAvailableRoom(@PathVariable int pageNumber) throws IllegalArgumentException {
        try{
            return ResponseEntity.ok(service.getAvailableRoom(pageNumber));
        }catch (Exception e) {
            throw new IllegalArgumentException("Something went wrong");
        }
    }
}

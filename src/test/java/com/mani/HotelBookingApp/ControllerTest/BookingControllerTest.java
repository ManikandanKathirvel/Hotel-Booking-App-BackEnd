package com.mani.HotelBookingApp.ControllerTest;

import com.mani.HotelBookingApp.Controller.CustomerContoller.BookingController;
import com.mani.HotelBookingApp.DTO.ReservationDto;
import com.mani.HotelBookingApp.DTO.ReservationResponseDto;
import com.mani.HotelBookingApp.Exceptions.ResourceNotFoundException;
import com.mani.HotelBookingApp.Service.Customer.Booking.BookingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class BookingControllerTest {
    @InjectMocks
    private BookingController bookingController;
    @Mock
    private BookingService bookingService;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testBookingRoom_Success(){
        ReservationDto reservationDto=new ReservationDto();
        reservationDto.setRoomType("AC");
        reservationDto.setRoomName("Room No1");
        reservationDto.setUsername("mani@gmail.com");
        reservationDto.setCheckInDate(LocalDate.parse("2024-10-01"));
        reservationDto.setCheckOutDate(LocalDate.parse("2014-10-04"));
        reservationDto.setUserId(1L);
        reservationDto.setRoomId(1L);

        when(bookingService.postReservation(any(ReservationDto.class))).thenReturn(true);
        ResponseEntity<?> response=bookingController.bookingRoom(reservationDto);
       assertEquals(HttpStatus.OK,response.getStatusCode());
        verify(bookingService).postReservation(reservationDto);
    }

    @Test
    void testBookingRoom_Failure(){
        ReservationDto reservationDto=new ReservationDto();
        reservationDto.setUserId(1L);
        when(bookingService.postReservation(any(ReservationDto.class))).thenReturn(false);
        assertThrows(IllegalArgumentException.class,()->{
            bookingController.bookingRoom(reservationDto);
        });
        verify(bookingService,times(1)).postReservation(any(ReservationDto.class));
    }
    @Test
    void testGetAllBookingUserId_Success() throws ResourceNotFoundException {
        Long id=1L;
        int page=4;
        ReservationResponseDto reservationDto1=new ReservationResponseDto();
        reservationDto1.setTotalPages(4);
        reservationDto1.setPageNo(4);

        when(bookingService.getAllReservationByUserId(id,page)).thenReturn(reservationDto1);
        ResponseEntity<?> response=bookingController.getAllBookingUserId(id,page);
        assertNotNull(response);
        assertEquals(HttpStatus.OK,response.getStatusCode());
    }
    @Test
    void testGetAllBookingById_failure(){
        Long id=1L;
        int page=4;
        when(bookingService.getAllReservationByUserId(id,page)).thenThrow(new RuntimeException("Resource Not Found"));
        Exception exception=assertThrows(ResourceNotFoundException.class,()->{
            bookingController.getAllBookingUserId(id,page);
        });
        assertEquals("Resource Not Found",exception.getMessage());
        verify(bookingService).getAllReservationByUserId(id,page);
    }





}

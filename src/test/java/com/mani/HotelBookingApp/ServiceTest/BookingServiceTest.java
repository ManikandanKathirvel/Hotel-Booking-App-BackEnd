package com.mani.HotelBookingApp.ServiceTest;

import com.mani.HotelBookingApp.DTO.ReservationDto;
import com.mani.HotelBookingApp.DTO.ReservationResponseDto;
import com.mani.HotelBookingApp.Entity.Reservation;
import com.mani.HotelBookingApp.Entity.Room;
import com.mani.HotelBookingApp.Entity.User;
import com.mani.HotelBookingApp.Enum.ReservationStatus;
import com.mani.HotelBookingApp.Repository.ReservationRepo;
import com.mani.HotelBookingApp.Repository.RoomRepo;
import com.mani.HotelBookingApp.Repository.UserRepo;
import com.mani.HotelBookingApp.Service.Admin.Reservation.ReservationService;
import com.mani.HotelBookingApp.Service.Customer.Booking.BookingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class BookingServiceTest {
    @InjectMocks
    BookingService bookingService;
    @Mock
    UserRepo userRepo;
    @Mock
    RoomRepo roomRepo;
    @Mock
    ReservationRepo reservationRepo;
    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
    }
    @Test
    void testPostReservation_success(){
        ReservationDto reservationDto=new ReservationDto();
        reservationDto.setUserId(1L);
        reservationDto.setRoomId(1L);
        reservationDto.setUsername("mani");
        reservationDto.setCheckInDate(LocalDate.of(2014,01,10));
        reservationDto.setCheckOutDate(LocalDate.of(2014,01,12));
        reservationDto.setRoomName("Room No1");
        reservationDto.setRoomType("AC");
        reservationDto.setPrice(100L);
        reservationDto.setReservationStatus(ReservationStatus.PENDING);

        User user= new User();
        Room room=new Room();
        room.setPrice(100L);
        when(userRepo.findById(1L)).thenReturn(Optional.of(user));
        when(roomRepo.findById(1L)).thenReturn(Optional.of(room));
        boolean result=bookingService.postReservation(reservationDto);
        assertTrue(result);
        verify(reservationRepo).save(any(Reservation.class));
    }
//    @Test
//    void testPostReservation_UserNotFound(){
//        ReservationDto reservationDto=new ReservationDto();
//        reservationDto.setUserId(1L);
//        reservationDto.setRoomId(1L);
//        reservationDto.setUsername("mani");
//        reservationDto.setCheckInDate(LocalDate.ofEpochDay(2014-01-10));
//        reservationDto.setCheckOutDate(LocalDate.ofEpochDay(2014-01-11));
//        reservationDto.setRoomName("Room No1");
//        reservationDto.setRoomType("AC");
//        reservationDto.setPrice(100L);
//        reservationDto.setReservationStatus(ReservationStatus.PENDING);
//        when(userRepo.findById(1L)).thenReturn(Optional.empty());
//        when(roomRepo.findById(1L)).thenReturn(Optional.of(new Room()));
//        boolean result=bookingService.postReservation(reservationDto);
//        assertFalse(result);
//        verify(reservationRepo,never()).save(any(Reservation.class));
//    }
    @Test
    void testPostReservation_RoomNotFound(){
        ReservationDto reservationDto=new ReservationDto();
        reservationDto.setUserId(1L);
        reservationDto.setRoomId(1L);
        reservationDto.setUsername("mani");
        reservationDto.setCheckInDate(LocalDate.of(2014,01,10));
        reservationDto.setCheckOutDate(LocalDate.of(2014,01,11));
        reservationDto.setRoomName("Room No1");
        reservationDto.setRoomType("AC");
        reservationDto.setPrice(100L);
        reservationDto.setReservationStatus(ReservationStatus.PENDING);

        when(userRepo.findById(1L)).thenReturn(Optional.of(new User()));
        when(roomRepo.findById(1L)).thenReturn(Optional.empty());

        boolean result=bookingService.postReservation(reservationDto);
        assertFalse(result);
        verify(reservationRepo,never()).save(any(Reservation.class));
    }
    @Test
    void testPostReservation_InvalidDates(){
        ReservationDto reservationDto=new ReservationDto();
        reservationDto.setUserId(1L);
        reservationDto.setRoomId(1L);
        reservationDto.setUsername("mani");
        reservationDto.setCheckInDate(LocalDate.of(2014,01,10));
        reservationDto.setCheckOutDate(LocalDate.of(2014,01,1));
        reservationDto.setRoomName("Room No1");
        reservationDto.setRoomType("AC");
        reservationDto.setPrice(100L);
        reservationDto.setReservationStatus(ReservationStatus.PENDING);
        Room room= new Room();
        room.setPrice(1000L);

        when(userRepo.findById(1L)).thenReturn(Optional.of(new User()));
        when(roomRepo.findById(1L)).thenReturn(Optional.of(room));
        boolean result=bookingService.postReservation(reservationDto);
        assertFalse(result);
        verify(reservationRepo,never()).save(any(Reservation.class));
    }
    @Test
    void testPostReservation_CalculatePriceCorrectly(){
        ReservationDto reservationDto=new ReservationDto();
        reservationDto.setUserId(1L);
        reservationDto.setRoomId(1L);
        reservationDto.setUsername("mani");
        reservationDto.setCheckInDate(LocalDate.ofEpochDay(2014-01-11));
        reservationDto.setCheckOutDate(LocalDate.ofEpochDay(2014-01-10));
        reservationDto.setRoomName("Room No1");
        reservationDto.setRoomType("AC");
        reservationDto.setPrice(1000L);
        reservationDto.setReservationStatus(ReservationStatus.PENDING);

        User user= new User();
        Room room=new Room();
        room.setPrice(1000L);
        when(userRepo.findById(1L)).thenReturn(Optional.of(user));
        when(roomRepo.findById(1L)).thenReturn(Optional.of(room));
        boolean result=bookingService.postReservation(reservationDto);
        assertTrue(result);
        verify(reservationRepo).save(argThat(reservation-> reservation.getPrice()==1000L));
    }
    @Test
    void testGetAllReservationByUserId_Success(){
        Long userId = 1L;
        int pageNo = 0;
        Pageable pageable = PageRequest.of(pageNo, ReservationService.SEARCH_RESULT_PER_PAGE);

        User user=new User();
        user.setId(1L);

        Room room=new Room();
        room.setId(1L);

        Reservation reservation1=new Reservation();
        reservation1.setPrice(100L);
        reservation1.setUser(user);
        reservation1.setRoom(room);

        Reservation reservation2= new Reservation();
        reservation2.setPrice(200L);
        reservation2.setUser(user);
        reservation2.setRoom(room);

        List<Reservation> reservationPage=Arrays.asList(reservation1,reservation2);
        Page<Reservation> reservations=new PageImpl<>(reservationPage,pageable,reservationPage.size());

        when(reservationRepo.findAllByUserId(pageable,userId)).thenReturn(reservations);

        ReservationResponseDto result=bookingService.getAllReservationByUserId(userId,pageNo);
        assertNotNull(result);
        assertEquals(2,result.getReservationDto().size());
        assertEquals(pageNo,result.getPageNo());
        assertEquals(1,result.getTotalPages());
        verify(reservationRepo).findAllByUserId(pageable,userId);
    }
    @Test
    void testGetAllReservationByUserId_NoReservations(){
        Long userId = 1L;
        int pageNo = 0;
        Pageable pageable = PageRequest.of(pageNo, ReservationService.SEARCH_RESULT_PER_PAGE);
        Page<Reservation> reservationPage=new PageImpl<>(Arrays.asList(),pageable,0);
        when(reservationRepo.findAllByUserId(pageable,userId)).thenReturn(reservationPage);
        ReservationResponseDto result=bookingService.getAllReservationByUserId(userId,pageNo);
        assertNotNull(result);
        assertTrue(result.getReservationDto().isEmpty());
        assertEquals(pageNo,result.getPageNo());
        assertEquals(reservationPage.getTotalPages(),result.getTotalPages());
        verify(reservationRepo).findAllByUserId(pageable,userId);
    }
    @Test
    void testGetAllReservationByUserId_InvalidUserId(){
        Long userId=0L;
        int pageNo=0;
        Pageable pageable=PageRequest.of(pageNo,10);
        Page<Reservation> reservationPage=new PageImpl<>(List.of(),pageable,0);
        when(reservationRepo.findAllByUserId(any(Pageable.class),eq(userId))).thenReturn(reservationPage);
        ReservationResponseDto result=bookingService.getAllReservationByUserId(userId, pageNo);
        assertNotNull(result);
        assertTrue(result.getReservationDto().isEmpty());
        assertEquals(pageNo,result.getPageNo());
        assertEquals(reservationPage.getTotalPages(),result.getTotalPages());
    }
  }

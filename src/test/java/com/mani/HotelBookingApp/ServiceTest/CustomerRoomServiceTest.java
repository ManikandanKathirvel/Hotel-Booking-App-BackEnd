package com.mani.HotelBookingApp.ServiceTest;

import com.mani.HotelBookingApp.DTO.RoomResponse;
import com.mani.HotelBookingApp.Entity.Room;
import com.mani.HotelBookingApp.Repository.RoomRepo;
import com.mani.HotelBookingApp.Service.Customer.Room.CustomerRoomService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

 class CustomerRoomServiceTest {
    @InjectMocks
    CustomerRoomService customerRoomService;
    @Mock
    RoomRepo roomRepo;
    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
    }
    @Test
    void testGetAvailableRoom_Success(){
        int page=0;
        int pageSize=3;
        Pageable pageable= PageRequest.of(page,pageSize);
        Room room1 = new Room("Room 1", 100.0, "Deluxe", true);
        Room room2 = new Room("Room 2", 150.0, "Standard", true);
        Room room3 = new Room("Room 3", 200.0, "Suite", true);

        List<Room> rooms= Arrays.asList(room1,room2,room3);
        Page<Room> roomPage=new PageImpl<>(rooms,pageable,rooms.size());
        when(roomRepo.findByAvailable(true,pageable)).thenReturn(roomPage);
        RoomResponse roomResponse=customerRoomService.getAvailableRoom(page);
        assertNotNull(roomResponse);
        assertEquals(3,roomResponse.getRoomDtoList().size());
        assertEquals(page,roomResponse.getPageNumber());
        assertEquals(1,roomResponse.getTotalPages());
        verify(roomRepo,times(1)).findByAvailable(true,pageable);
    }
    @Test
    void testGetAvailableRoom_EmptyPage(){
        int pageNo=1;
        int pageSize=3;
        Pageable pageable=PageRequest.of(pageNo,pageSize);
        Page<Room> roomPage=Page.empty(pageable);
        when(roomRepo.findByAvailable(true,pageable)).thenReturn(roomPage);
        RoomResponse roomResponse=customerRoomService.getAvailableRoom(pageNo);
        assertNotNull(roomResponse);
        assertEquals(pageNo,roomResponse.getPageNumber());
        assertEquals(0,roomResponse.getRoomDtoList().size());
        assertEquals(0,roomResponse.getTotalPages());
        verify(roomRepo,times(1)).findByAvailable(true,pageable);
    }


}

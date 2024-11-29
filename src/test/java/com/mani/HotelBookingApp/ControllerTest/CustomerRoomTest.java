package com.mani.HotelBookingApp.ControllerTest;

import com.mani.HotelBookingApp.Controller.CustomerContoller.CustomerRoom;
import com.mani.HotelBookingApp.DTO.RoomResponse;
import com.mani.HotelBookingApp.Exceptions.IllegalArgumentException;
import com.mani.HotelBookingApp.Service.Customer.Room.CustomerRoomService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
 class CustomerRoomTest {
    @InjectMocks
    private CustomerRoom customerRoom;
    @Mock
    private CustomerRoomService service;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
    }
    @Test
    void testGetAvailableRooms_Success() throws IllegalArgumentException {
        int page =4;
        RoomResponse roomResponse=new RoomResponse();
       roomResponse.setPageNumber(4);
       roomResponse.setTotalPages(4);

        when(service.getAvailableRoom(page)).thenReturn(roomResponse);
        ResponseEntity<?> response=customerRoom.getAvailableRoom(page);
        assertEquals(200,response.getStatusCodeValue());
        assertEquals(roomResponse,response.getBody());
        verify(service).getAvailableRoom(page);
    }

    @Test
    void testGetAvailableRooms_Exception() {
        int page=4;
        when(service.getAvailableRoom(page)).thenThrow(new RuntimeException("Room Not Found"));
        Exception exception=assertThrows(IllegalArgumentException.class,()->{
            customerRoom.getAvailableRoom(page);
        });
        assertEquals("Something went wrong",exception.getMessage());
        verify(service).getAvailableRoom(page);

    }
}

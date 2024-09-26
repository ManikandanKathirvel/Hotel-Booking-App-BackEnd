package com.mani.HotelBookingApp.ControllerTest;

import com.mani.HotelBookingApp.Controller.AdminController.RoomController;
import com.mani.HotelBookingApp.DTO.RoomDTO;
import com.mani.HotelBookingApp.DTO.RoomResponse;
import com.mani.HotelBookingApp.Entity.Room;
import com.mani.HotelBookingApp.Exceptions.ResourceNotFoundException;
import com.mani.HotelBookingApp.Service.Admin.Rooms.RoomService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class RoomControllerTest {
    @InjectMocks
    RoomController roomController;
    @Mock
    RoomService service;
    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
    }
    @Test
    public void testPostRooms_Success(){
        RoomDTO roomDTO=new RoomDTO();
        roomDTO.setPrice(1000L);
        roomDTO.setName("Room No2");
        roomDTO.setAvailable(true);
        roomDTO.setType("AC");
        when(service.postRooms(roomDTO)).thenReturn(true);
        ResponseEntity<?> response=roomController.postRooms(roomDTO);
        assertEquals(HttpStatus.OK,response.getStatusCode());
        verify(service).postRooms(roomDTO);
    }
    @Test
    public void testPostRoom_InvalidData(){
        RoomDTO roomDTO=new RoomDTO();
        roomDTO.setPrice(-1000L);
        roomDTO.setName("RoomNo2");
        roomDTO.setAvailable(false);
        roomDTO.setType("AC");
        when(service.postRooms(roomDTO)).thenReturn(false);
        IllegalArgumentException exception=assertThrows(IllegalArgumentException.class,()->{
            roomController.postRooms(roomDTO);
        });
        assertEquals("Invalid Room Data",exception.getMessage());
        verify(service).postRooms(roomDTO);
    }
    @Test
    public void testGetAllRoom_success(){
        int pageNo=1;

       RoomResponse roomDTOList=new RoomResponse();
     when(service.getAllRooms(pageNo)).thenReturn(roomDTOList);

        ResponseEntity<?> response=roomController.getAllRoom(pageNo);
        assertEquals(200,response.getStatusCodeValue());
        assertEquals(roomDTOList,response.getBody());
        verify(service).getAllRooms(pageNo);
    }

    @Test
    public void testGetAllRooms_PageOutOfBound(){
        int pageNo=-1;
        ResponseEntity<?> response=roomController.getAllRoom(pageNo);
        assertEquals(400,response.getStatusCodeValue());
        assertEquals("Invalid Room Data",response.getBody());
    }
    @Test
    public void testGetAllRooms_EmptyRoom(){
        int pageNo=1;
        RoomResponse roomResponse=new RoomResponse();
        when(service.getAllRooms(pageNo)).thenReturn(roomResponse);
        ResponseEntity<?> response=roomController.getAllRoom(pageNo);
        assertEquals(200,response.getStatusCodeValue());
        assertEquals(roomResponse,response.getBody());
        verify(service).getAllRooms(pageNo);
    }
    @Test
    public void testGetById_Success(){
        Long roomId=1L;
        RoomDTO room=new RoomDTO();
        when(service.getRoomById(roomId)).thenReturn(room);
        ResponseEntity<?> response=roomController.getById(roomId);
        assertEquals(200,response.getStatusCodeValue());
        assertEquals(room,response.getBody());
        verify(service).getRoomById(roomId);
    }
    @Test
    public void testGetById_RoomNOtFound(){
        Long roomId=1L;
        when(service.getRoomById(roomId)).thenThrow(new EntityNotFoundException("Room Not Found"));
        ResponseEntity<?> response=roomController.getById(roomId);
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    }
    @Test
    public void testGetById_Exception(){
        Long roomId=1L;
        when(service.getRoomById(roomId)).thenThrow(new RuntimeException("Room Not Found"));
        ResponseEntity<?> response=roomController.getById(roomId);
        assertEquals(HttpStatus.BAD_REQUEST,response.getStatusCode());
    }

    @Test
    public void testUpdateRoom_Success() throws ResourceNotFoundException {
        Long id=1L;
        RoomDTO roomDTO=new RoomDTO();
        roomDTO.setPrice(-1000L);
        roomDTO.setName("RoomNo2");
        roomDTO.setAvailable(false);
        roomDTO.setType("AC");
        when(service.updateRoom(eq(id),any(RoomDTO.class))).thenReturn(true);
        ResponseEntity<?> response=roomController.updateRoom(id,roomDTO);
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertTrue((Boolean)response.getBody());
        verify(service).updateRoom(id,roomDTO);
    }
    @Test
    public void testUpdateRoom_NotFound(){
        Long id=1L;
        RoomDTO roomDTO=new RoomDTO();
        roomDTO.setPrice(-1000L);
        roomDTO.setName("RoomNo2");
        roomDTO.setAvailable(false);
        roomDTO.setType("AC");
        when(service.updateRoom(eq(id),any(RoomDTO.class))).thenReturn(false);
        Exception exception=assertThrows(ResourceNotFoundException.class,()->{
            roomController.updateRoom(id,roomDTO);});
        assertEquals("Room not found with ID: "+id,exception.getMessage());
        verify(service).updateRoom(id,roomDTO);
    }
    @Test
    public void testDeleteRoom_Success(){
        Long id=1L;
        ResponseEntity<?> response=roomController.deleteRoom(id);
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals("successfully deleted",response.getBody());
        verify(service).deleteRoom(id);
    }
    @Test
    public void testDeleteRoom_NotFound(){
        Long id=1L;
        doThrow(new EntityNotFoundException("Room Not Found")).when(service).deleteRoom(id);
        ResponseEntity<?> response=roomController.deleteRoom(id);
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
        assertEquals("Room Not Found",response.getBody());
        verify(service).deleteRoom(id);
    }
    @Test
    public void testDeleteRoom_exception(){
        Long id=1L;
        doThrow(new RuntimeException("Some Error")).when(service).deleteRoom(id);
        ResponseEntity<?> response=roomController.deleteRoom(id);
        assertEquals(HttpStatus.BAD_REQUEST,response.getStatusCode());
        assertEquals("Some Error",response.getBody());
        verify(service).deleteRoom(id);
    }




}

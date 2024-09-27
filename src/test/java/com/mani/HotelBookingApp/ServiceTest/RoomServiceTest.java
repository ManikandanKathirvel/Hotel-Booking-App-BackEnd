package com.mani.HotelBookingApp.ServiceTest;

import com.mani.HotelBookingApp.DTO.RoomDTO;
import com.mani.HotelBookingApp.DTO.RoomResponse;
import com.mani.HotelBookingApp.Entity.Room;
import com.mani.HotelBookingApp.Repository.RoomRepo;
import com.mani.HotelBookingApp.Service.Admin.Rooms.RoomService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

 class RoomServiceTest {
    @InjectMocks
    RoomService service;
    @Mock
    RoomRepo roomRepo;
    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
     void testPostRoom_Success(){
        RoomDTO roomDTO = new RoomDTO();
        roomDTO.setName("Room No1");
        roomDTO.setType("AC");
        roomDTO.setPrice(1000L);
        boolean result=service.postRooms(roomDTO);
        assertTrue(result);
        verify(roomRepo).save(any(Room.class));
    }
    @Test
     void testPostRoom_NullRoomDto(){
        boolean result=service.postRooms(null);
        assertFalse(result);
    }
    @Test
     void testPostRoom_RoomDTOWithNullPointer(){
        RoomDTO roomDTO= new RoomDTO();
        roomDTO.setName(null);
        roomDTO.setType(null);
        roomDTO.setPrice(null);
        boolean result=service.postRooms(roomDTO);
        assertTrue(result);
    }
    @Test
     void testPostRoom_ExceptionDuringSave(){
        RoomDTO roomDTO = new RoomDTO();
        roomDTO.setName("Room No1");
        roomDTO.setType("AC");
        roomDTO.setPrice(1000L);
        doThrow(new RuntimeException("Database Error")).when(roomRepo).save(any(Room.class));
        boolean result=service.postRooms(roomDTO);
        assertFalse(result);
    }
    @Test
     void testPostRoom_NegativePrice(){
        RoomDTO roomDTO=new RoomDTO();
        roomDTO.setPrice(-900L);
        roomDTO.setName("Room No1");
        roomDTO.setType("AC");

        boolean result= service.postRooms(roomDTO);
        assertTrue(result);
    }

    @Test
     void  testGetAllRooms_success(){
        int pageNumber=0;
        int pageSize=4;
        Room room=new Room();
        room.setName("Room No1");
        room.setType("AC");
        room.setPrice(1000L);
        room.setId(1L);
        room.setAvailable(true);

        Room room1=new Room();
        room1.setName("Room No2");
        room1.setType("AC");
        room1.setAvailable(true);
        room1.setId(2L);
        room1.setPrice(1000L);

        List<Room> rooms= Arrays.asList(room1,room);
        Page<Room> roomPage=new PageImpl<>(rooms, PageRequest.of(pageNumber,pageSize),1);

        when(roomRepo.findAll(PageRequest.of(pageNumber,pageSize))).thenReturn(roomPage);

        RoomResponse roomResponse= service.getAllRooms(pageNumber);
        List<String> roomNames=roomResponse.getRoomDtoList().stream().map(RoomDTO::getName).collect(Collectors.toList());//NOSONAR
        List<String> expectedRoomName=Arrays.asList("Room No1","Room No2");
        assertTrue(expectedRoomName.containsAll(roomNames));
        assertEquals(2,roomResponse.getRoomDtoList().size());
    }
    @Test
     void testGetAllRooms_EmptyPage(){
        int pageNo=0;
        List<Room> rooms=Arrays.asList();
        Page<Room> roomPage=new PageImpl<>(rooms,PageRequest.of(pageNo,4),0);
        when(roomRepo.findAll(PageRequest.of(pageNo,4))).thenReturn(roomPage);
        RoomResponse roomResponse=service.getAllRooms(pageNo);
        assertEquals(0,roomResponse.getRoomDtoList().size());
    }
    @Test
     void testGetAllRooms_PageNoOutOfBound(){
        int pageNo=10;
        List<Room> rooms=Arrays.asList();
        Page<Room> roomPage = new PageImpl<>(rooms,PageRequest.of(pageNo,4),0);
        when(roomRepo.findAll(PageRequest.of(pageNo,4))).thenReturn(roomPage);
        RoomResponse roomResponse = service.getAllRooms(pageNo);
        assertEquals(10,roomResponse.getPageNumber());
        assertEquals(0,roomResponse.getTotalPages());
        assertTrue(roomResponse.getRoomDtoList().isEmpty());
        verify(roomRepo).findAll(PageRequest.of(pageNo,4));
    }

    @Test
     void testUpdateRooms_success(){
        Long roomId=1L;
        RoomDTO roomDTO=new RoomDTO();
        roomDTO.setType("AC");
        roomDTO.setAvailable(true);
        roomDTO.setName("Room No1");
        roomDTO.setPrice(1000L);

        Room room=new Room();
        room.setId(roomId);
        room.setAvailable(true);
        room.setPrice(1000L);
        room.setType("Non AC");
        room.setName("Room No2");

        when(roomRepo.findById(roomId)).thenReturn(Optional.of(room));
        boolean result=service.updateRoom(roomId,roomDTO);
        assertTrue(result);
        assertEquals(roomDTO.getType(),room.getType());
        assertEquals(roomDTO.getPrice(),room.getPrice());
        assertEquals(roomDTO.getName(),room.getName());
        verify(roomRepo).save(room);
    }

    @Test
     void testUpdateRooms_RoomNotFound(){
        Long roomId=1L;
        RoomDTO roomDTO=new RoomDTO();
        roomDTO.setPrice(1000L);
        roomDTO.setName("Room NO2");
        roomDTO.setAvailable(true);
        roomDTO.setType("AC");
        when(roomRepo.findById(roomId)).thenReturn(Optional.empty());
        boolean result=service.updateRoom(roomId,roomDTO);
        assertFalse(result);
        verify(roomRepo,never()).save(any(Room.class));
    }
    @Test
     void testUpdateRooms_Exception(){
        Long roomId=1L;
        RoomDTO roomDTO=new RoomDTO();
        roomDTO.setPrice(1000L);
        roomDTO.setName("Room NO2");
        roomDTO.setAvailable(true);
        roomDTO.setType("AC");
        when(roomRepo.findById(roomId)).thenThrow(new RuntimeException("DataBase Error"));
        Exception exception=assertThrows(RuntimeException.class,()->{
            service.updateRoom(roomId,roomDTO);
        });
        assertEquals("DataBase Error",exception.getMessage());
        verify(roomRepo,never()).save(any(Room.class));
    }
    @Test
     void testGetRoomById(){
        Long roomId=1L;
        Room room=new Room();
        room.setName("Room No1");
        room.setType("AC");
        room.setPrice(1000L);
        room.setAvailable(true);

        RoomDTO roomDTO=new RoomDTO();
        roomDTO.setPrice(1000L);
        roomDTO.setName("Room No2");
        roomDTO.setAvailable(true);
        roomDTO.setType("AC");
        when(roomRepo.findById(roomId)).thenReturn(Optional.of(room));

        RoomDTO result=service.getRoomById(roomId);
        assertNotNull(result);
        assertEquals(roomDTO.getType(),result.getType());
        assertEquals(roomDTO.getPrice(),result.getPrice());
        verify(roomRepo,times(1)).findById(roomId);
    }
    @Test
     void testGetRoomById_RoomNOtFound(){
        Long roomId=1L;
        when(roomRepo.findById(roomId)).thenReturn(Optional.empty());
        EntityNotFoundException exception=assertThrows( EntityNotFoundException.class,()->{
            service.getRoomById(roomId);
        });
        assertEquals("Room Not found",exception.getMessage());
        verify(roomRepo,times(1)).findById(roomId);
    }
    @Test
     void testDeleteRoom_success(){
        Long roomId=1L;
        Room room=new Room();
        when(roomRepo.findById(roomId)).thenReturn(Optional.of(room));
        service.deleteRoom(roomId);
        verify(roomRepo).deleteById(roomId);
    }
    @Test
     void testDeleteRoom_RoomNotFound(){
        Long roomId=1L;
        when(roomRepo.findById(roomId)).thenReturn(Optional.empty());
        EntityNotFoundException exception=assertThrows(EntityNotFoundException.class,()->{
            service.deleteRoom(roomId);
        });
        assertEquals("Room Not Found",exception.getMessage());
        verify(roomRepo,never()).deleteById(roomId);
    }
    @Test
     void testDeleteRoom_Exception(){
        Long roomId=1L;
        when(roomRepo.findById(roomId)).thenThrow(new RuntimeException("Database Error"));
        Exception exception=assertThrows(RuntimeException.class,()->{
            service.deleteRoom(roomId);
        });
        assertEquals("Database Error",exception.getMessage());
        verify(roomRepo,never()).deleteById(roomId);
    }
}

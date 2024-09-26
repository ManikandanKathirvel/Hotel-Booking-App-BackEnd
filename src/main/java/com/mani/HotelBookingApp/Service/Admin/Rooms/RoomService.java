package com.mani.HotelBookingApp.Service.Admin.Rooms;//NOSONAR

import com.mani.HotelBookingApp.DTO.RoomDTO;
import com.mani.HotelBookingApp.DTO.RoomResponse;
import com.mani.HotelBookingApp.Entity.Room;
import com.mani.HotelBookingApp.Repository.RoomRepo;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RoomService {

    private final RoomRepo repo;

    public RoomService(RoomRepo repo) {
        this.repo = repo;
    }

    public boolean postRooms(RoomDTO roomDTO) {
        try {
            Room room = new Room();
            room.setName(roomDTO.getName());
            room.setPrice(roomDTO.getPrice());
            room.setType(roomDTO.getType());
            room.setAvailable(true);
            repo.save(room);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public RoomResponse getAllRooms(int pageNumber) {
        final int pageSize = 4;
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Room> roomPage = repo.findAll(pageable);

        RoomResponse roomResponse = new RoomResponse();
        roomResponse.setPageNumber(roomPage.getPageable().getPageNumber());
        roomResponse.setTotalPages(roomPage.getTotalPages());
        roomResponse.setRoomDtoList(roomPage.getContent().stream().map(Room::getRoomDto).collect(Collectors.toList()));//NOSONAR
        return roomResponse;
    }

    public boolean updateRoom(Long id, RoomDTO roomDTO) {
        Optional<Room> room = repo.findById(id);
        if (room.isPresent()) {
            Room room1 = room.get();
            room1.setType(roomDTO.getType());
            room1.setName(roomDTO.getName());
            room1.setPrice(roomDTO.getPrice());
            repo.save(room1);
            return true;
        }
        return false;
    }

    public RoomDTO getRoomById(Long id) {
        Optional<Room> room = repo.findById(id);
        if (room.isPresent()) {
            return room.get().getRoomDto();
        }
        throw new EntityNotFoundException("Room Not found");
    }

    public void deleteRoom(Long id) {
        Optional<Room> room = repo.findById(id);
        if (room.isPresent()) {
            repo.deleteById(id);
        } else {
            throw new EntityNotFoundException("Room Not Found");
        }
    }
}

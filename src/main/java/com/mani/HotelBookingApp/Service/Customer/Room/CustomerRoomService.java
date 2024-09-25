package com.mani.HotelBookingApp.Service.Customer.Room;//NOSONAR

import com.mani.HotelBookingApp.DTO.RoomResponse;
import com.mani.HotelBookingApp.Entity.Room;
import com.mani.HotelBookingApp.Repository.RoomRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class CustomerRoomService {

    private final RoomRepo repo;

    public CustomerRoomService(RoomRepo repo) {
        this.repo = repo;
    }

    public RoomResponse getAvailableRoom(int pageNumber) {
        int pageSize = 3;
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Room> roomPage = repo.findByAvailable(true, pageable);

        RoomResponse roomResponse = new RoomResponse();
        roomResponse.setPageNumber(roomPage.getPageable().getPageNumber());
        roomResponse.setTotalPages(roomPage.getTotalPages());
        roomResponse.setRoomDtoList(roomPage.getContent().stream().map(Room::getRoomDto).collect(Collectors.toList()));//NOSONAR
        return roomResponse;

    }
}

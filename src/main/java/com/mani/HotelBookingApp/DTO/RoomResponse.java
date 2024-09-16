package com.mani.HotelBookingApp.DTO;

import java.util.List;

public class RoomResponse {

    private List<RoomDTO> roomDtoList;
    private Integer totalPages;
    private Integer pageNumber;

    public List<RoomDTO> getRoomDtoList() {
        return roomDtoList;
    }

    public void setRoomDtoList(List<RoomDTO> roomDtoList) {
        this.roomDtoList = roomDtoList;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }
}

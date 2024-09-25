package com.mani.HotelBookingApp.DTO;//NOSONAR

import java.util.List;

public class ReservationResponseDto {
    private Integer totalPages;
    private Integer pageNo;
    private List<ReservationDto> reservationDto;

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public List<ReservationDto> getReservationDto() {
        return reservationDto;
    }

    public void setReservationDto(List<ReservationDto> reservationDto) {
        this.reservationDto = reservationDto;
    }
}

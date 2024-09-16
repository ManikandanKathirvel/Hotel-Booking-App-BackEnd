package com.mani.HotelBookingApp.Entity;

import com.mani.HotelBookingApp.DTO.RoomDTO;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name, type;
    private Long price;
    private boolean available;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public RoomDTO getRoomDto() {
        RoomDTO roomDTO = new RoomDTO();
        roomDTO.setId(id);
        roomDTO.setName(name);
        roomDTO.setAvailable(available);
        roomDTO.setType(type);
        roomDTO.setPrice(price);
        return roomDTO;
    }
}

package com.mani.HotelBookingApp.ServiceTest;

import com.mani.HotelBookingApp.Entity.User;
import com.mani.HotelBookingApp.Repository.UserRepo;
import com.mani.HotelBookingApp.Service.Jwt.UserServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
class UserServiceImpleTest {
    @InjectMocks
    UserServiceImpl userServiceimpl;
    @Mock
    UserRepo userRepo;
    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
    }
    @Test
    void testLoadUserByUsername_Success(){
        String mail="mani@9789";
        User user=new User();
        user.setEmail(mail);
        user.setPassword("mani");
        when(userRepo.findByEmail(mail)).thenReturn(Optional.of(user));
        UserDetails userDetails=userServiceimpl.userDetailsService().loadUserByUsername(mail);
        assertNotNull(userDetails);
        assertNotNull(userDetails.getUsername());
        assertEquals(mail,userDetails.getUsername());
        verify(userRepo,times(1)).findByEmail(mail);
    }
    @Test
    void testLoadUserByUsername_UserNotFound(){
        String mail="mani@9789";
        User user=new User();
        user.setEmail(mail);
        user.setPassword("mani");
        when(userRepo.findByEmail(mail)).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class,()->{
            userServiceimpl.userDetailsService().loadUserByUsername(mail);
        });
        verify(userRepo,times(1)).findByEmail(mail);
    }
}
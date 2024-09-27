package com.mani.HotelBookingApp.ControllerTest;

import com.mani.HotelBookingApp.Controller.UserController;
import com.mani.HotelBookingApp.DTO.AuthenticationRequest;
import com.mani.HotelBookingApp.DTO.AuthenticationResponse;
import com.mani.HotelBookingApp.DTO.SignupRequest;
import com.mani.HotelBookingApp.DTO.UserDTO;
import com.mani.HotelBookingApp.Entity.User;
import com.mani.HotelBookingApp.Service.Jwt.UserServiceimpl;
import com.mani.HotelBookingApp.Service.UserService;
import com.mani.HotelBookingApp.Util.JWTUtils;
import jakarta.persistence.EntityNotFoundException;
import org.hibernate.sql.ast.tree.expression.CaseSimpleExpression;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserControllerTest {
    @InjectMocks
    private UserController userController;
    private AuthenticationRequest authRequest;
    private UserDetails userDetails;
    private User user;
    private String generatedJwt;
    @Mock
    private UserService service;
    @Mock
    private AuthenticationManager manager;
    @Mock
    JWTUtils jwtUtils;
    @Mock
    UserDetailsService userDetailsService;
    @Mock
    UserServiceimpl userServiceimpl;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
        authRequest = new AuthenticationRequest();
        authRequest.setEmail("mani@gmail.com");
        authRequest.setPassword("mani");

        userDetails = mock(UserDetails.class);

        user = new User();
        user.setEmail("mani@gmail.com");
        user.setPassword("mani");
        user.setId(1L);

        generatedJwt = "fake-jwt-token";
    }
    @Test
    void testSignUp_Success(){
        SignupRequest signupRequest= new SignupRequest();
        signupRequest.setUsername("mani");
        signupRequest.setEmail("mani@gmail.com");
        signupRequest.setPassword("mani");

        UserDTO dto=new UserDTO();
        dto.setEmail("mani@gmail.com");
        dto.setPassword("mani");

        when(service.createUser(signupRequest)).thenReturn(dto);
        ResponseEntity<?> response=userController.signUp(signupRequest);
        assertEquals(200,response.getStatusCodeValue());
        assertEquals(HttpStatus.OK,response.getStatusCode());
        verify(service,times(1)).createUser(any(SignupRequest.class));
    }
    @Test
    void testSignUp_Failure(){
        SignupRequest signupRequest= new SignupRequest();
        signupRequest.setUsername("mani@gmail.com");
        signupRequest.setEmail("mani@gmail.com");
        signupRequest.setPassword("mani");

        UserDTO dto=new UserDTO();
        dto.setEmail("mani@gmail.com");
        dto.setPassword("mani");
        when(service.createUser(any(SignupRequest.class))).thenThrow(new EntityNotFoundException("User Already Exist"));
        ResponseEntity<?> response=userController.signUp(signupRequest);
        assertEquals(HttpStatus.NOT_ACCEPTABLE,response.getStatusCode());
        assertEquals("Email id not available",response.getBody());
        verify(service,times(1)).createUser(any(SignupRequest.class));
    }
    @Test
    void testSignUp_GenericException(){
        SignupRequest signupRequest= new SignupRequest();
        signupRequest.setUsername("mani");
        signupRequest.setEmail("mani@com");
        signupRequest.setPassword("mani");

        when(service.createUser(any(SignupRequest.class))).thenThrow(new RuntimeException("DataBase Error"));
        ResponseEntity<?> response=userController.signUp(signupRequest);
        assertEquals(HttpStatus.NOT_ACCEPTABLE,response.getStatusCode());
        assertEquals("Email id not available",response.getBody());
        verify(service,times(1)).createUser(any(SignupRequest.class));
    }

    @Test
    void testCreateAuthenticationToken_success(){
        when(manager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(null);
        when(userServiceimpl.userDetailsService()).thenReturn(mock(UserDetailsService.class));
        when(userServiceimpl.userDetailsService().loadUserByUsername(authRequest.getEmail())).thenReturn(userDetails);
        when(service.findByEmail(userDetails.getUsername())).thenReturn(Optional.of(user));
        when(jwtUtils.generateToken(userDetails)).thenReturn(generatedJwt);

        ResponseEntity<AuthenticationResponse> response= userController.createAuthenticationToken(authRequest);

        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(generatedJwt,response.getBody().getJwt());
        assertEquals(user.getUserRole(),response.getBody().getUserRole());
        assertEquals(user.getId(),response.getBody().getUserId());
    }
    @Test
    void testCreateAuthenticationToken_InvalidCredential(){
        when(manager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenThrow(new BadCredentialsException("incorrect email or password"));
        BadCredentialsException exception=assertThrows(BadCredentialsException.class,()->{
            userController.createAuthenticationToken(authRequest);
        });
        assertEquals("incorrect email or password",exception.getMessage());
    }
    @Test
    void testCreateAuthenticationToken_UserNotFound() {
        // Arrange
        when(manager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(null);
        UserDetails userDetails1=mock(UserDetails.class.getName());
        when(userDetails1.getUsername()).thenReturn(authRequest.getEmail());
        when(userServiceimpl.userDetailsService()).thenReturn(mock(UserDetailsService.class));
        when(userServiceimpl.userDetailsService().loadUserByUsername(authRequest.getEmail())).thenReturn(userDetails);
        when(service.findByEmail(userDetails.getUsername())).thenReturn(Optional.empty());
        when(jwtUtils.generateToken(userDetails)).thenReturn(generatedJwt);

        ResponseEntity<AuthenticationResponse> response = userController.createAuthenticationToken(authRequest);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response);
        assertNull(response.getBody().getUserRole());
        assertNull(response.getBody().getUserId());
    }
    }

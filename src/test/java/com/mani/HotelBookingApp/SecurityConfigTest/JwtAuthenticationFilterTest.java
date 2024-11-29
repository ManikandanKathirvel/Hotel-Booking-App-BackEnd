package com.mani.HotelBookingApp.SecurityConfigTest;

import com.mani.HotelBookingApp.SecurityConfig.JwtAuthenticationFilter;
import com.mani.HotelBookingApp.Service.Jwt.UserServiceImpl;
import com.mani.HotelBookingApp.Util.JWTUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class JwtAuthenticationFilterTest {
    @InjectMocks
    JwtAuthenticationFilter  jwtAuthenticationFilter;
    @Mock
    private JWTUtils jwtUtils;
    @Mock
    private FilterChain filterChain;

    @Mock
    private UserServiceImpl serviceimpl;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.clearContext();
    }
//    @Test
//    void testDoFilterInternal_ValidJwt() throws ServletException, IOException {
//        // Arrange
//        MockHttpServletRequest request = new MockHttpServletRequest();
//        MockHttpServletResponse response = new MockHttpServletResponse();
//        request.addHeader("Authorization", "Bearer valid_jwt_token");
//
//        UserDetailsService userDetailsService = mock(UserDetailsService.class);
//        UserDetails userDetails=new User("mani@123","mani",new ArrayList<>());
//
//        when(serviceimpl.userDetailsService()).thenReturn(userDetailsService);
//        when(jwtUtils.extractUserName("valid_jwt_token")).thenReturn("mani@123");
//        when(serviceimpl.userDetailsService().loadUserByUsername("mani@123")).thenReturn(userDetails);
//        when(jwtUtils.isTokenValid("valid_jwt_token", userDetails)).thenReturn(true);
//
//        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);
//        System.out.println(SecurityContextHolder.getContext().getAuthentication());
//        UsernamePasswordAuthenticationToken token= (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
//
//        assertNotNull(token, "Expected a non-null authentication token");
//        assertEquals("mani@123", token.getName(), "Expected the principal to be 'mani@123'");
//
//        verify(filterChain).doFilter(request, response);
//    }
    @Test
    void testDoFilterInternal_InvalidToken() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        request.addHeader("Authorization", "Bearer invalid_jwt_token");
        UserDetailsService userDetailsService=mock(UserDetailsService.class);
        UserDetails userDetails=new User("mani@123","mani",new ArrayList<>());

        when(jwtUtils.extractUserName("invalid_jwt_token")).thenReturn("mani@123");
        when(serviceimpl.userDetailsService()).thenReturn(userDetailsService);
        when(userDetailsService.loadUserByUsername("mani@123")).thenReturn(userDetails);
        when(jwtUtils.isTokenValid("invalid_jwt_token",userDetails)).thenReturn(false);

        jwtAuthenticationFilter.doFilterInternal(request,response,filterChain);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain).doFilter(request,response);
    }
    @Test
    void testDoFilterInternal_NoAuthHeader() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        request.addHeader("Authorization", "Bearer invalid_jwt_token");
        UserDetailsService userDetailsService=mock(UserDetailsService.class);
        UserDetails userDetails=new User("mani@123","mani",new ArrayList<>());
        jwtAuthenticationFilter.doFilterInternal(request,response,filterChain);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain).doFilter(request,response);
    }
    @Test
    void testDoFilterInternal_AuthHeaderWithoutBearer() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        request.addHeader("Authorization", "Basic credentials");

        jwtAuthenticationFilter.doFilterInternal(request,response,filterChain);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain).doFilter(request,response);
    }

}

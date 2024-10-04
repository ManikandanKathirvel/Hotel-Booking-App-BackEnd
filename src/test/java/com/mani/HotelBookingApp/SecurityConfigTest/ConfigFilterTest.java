package com.mani.HotelBookingApp.SecurityConfigTest;

import com.mani.HotelBookingApp.SecurityConfig.ConfigFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockFilterConfig;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

public class ConfigFilterTest {
    @InjectMocks
    ConfigFilter configFilter;
    @Mock
    private HttpServletResponse response;
    private HttpServletRequest request;
    private FilterChain filterChain;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
        configFilter =new ConfigFilter();
        request= Mockito.mock(HttpServletRequest.class);
        response=Mockito.mock(HttpServletResponse.class);
        filterChain=Mockito.mock(FilterChain.class);
    }
    @Test
    void testInit(){
        FilterConfig filterConfig=new MockFilterConfig();
        assertDoesNotThrow(()->configFilter.init(filterConfig));
    }
    @Test
    void testDoFilter_WithOptionsMethod() throws ServletException, IOException {
        when(request.getHeader("Origin")).thenReturn("http://example.com");
        when(request.getMethod()).thenReturn("OPTIONS");
        configFilter.doFilter(request,response,filterChain);
        verify(response).setHeader("Access-Control-Allow-Origin", "http://example.com");
        verify(response).setHeader("Access-Control-Allow-Methods", "POST,GET,PUT,DELETE,OPTIONS");
        verify(response).setHeader("Access-Control-Max-Age", "3600");
        verify(response).setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type, Accept, Origin, X-Requested-With");
        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(filterChain,never()).doFilter(request,response);
    }
    @Test
    void testDoFilter_WithOtherMethod() throws ServletException, IOException {
        when(request.getHeader("Origin")).thenReturn("http://example.com");
        when(request.getMethod()).thenReturn("GET");
        configFilter.doFilter(request,response,filterChain);
        verify(response).setHeader("Access-Control-Allow-Origin", "http://example.com");
        verify(response).setHeader("Access-Control-Allow-Methods", "POST,GET,PUT,DELETE,OPTIONS");
        verify(response).setHeader("Access-Control-Max-Age", "3600");
        verify(response).setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type, Accept, Origin, X-Requested-With");
        verify(filterChain).doFilter(request, response);
    }
    @Test
    void testDoFilter_NoOriginHeader() throws ServletException, IOException {
        when(request.getMethod()).thenReturn("GET");
        configFilter.doFilter(request,response,filterChain);
        verify(response).setHeader("Access-Control-Allow-Origin", null); // Expect null for missing Origin
        verify(response).setHeader("Access-Control-Allow-Methods", "POST,GET,PUT,DELETE,OPTIONS");
        verify(response).setHeader("Access-Control-Max-Age", "3600");
        verify(response).setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type, Accept, Origin, X-Requested-With");
        verify(filterChain).doFilter(request,response);
    }
}

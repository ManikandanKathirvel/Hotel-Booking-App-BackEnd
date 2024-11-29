package com.mani.HotelBookingApp.SecurityConfigTest;

import com.mani.HotelBookingApp.SecurityConfig.JwtAuthenticationFilter;
import com.mani.HotelBookingApp.SecurityConfig.WebConfig;
import com.mani.HotelBookingApp.Service.Jwt.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
@SpringBootTest
public class WebConfigTest {

    private WebConfig webConfig;
    private UserServiceImpl serviceimpl;
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    private HttpSecurity httpSecurity;

    @BeforeEach
    void setUp() throws Exception {
        serviceimpl = mock(UserServiceImpl.class);
        jwtAuthenticationFilter = mock(JwtAuthenticationFilter.class);
        webConfig = new WebConfig(serviceimpl, jwtAuthenticationFilter);
        httpSecurity = mock(HttpSecurity.class);

    }
//    @Test
//    void testSecurityFilterChainConfiguration() throws Exception {
//
//        SecurityFilterChain securityFilterChain=webConfig.securityFilterChain(httpSecurity);
//        assertThat(securityFilterChain).isNotNull();
//        assertThat(httpSecurity).isNotNull();
//        assertThat(httpSecurity.getConfigurer(CsrfConfigurer.class).disable());
//        SessionManagementConfigurer<?> configurer=httpSecurity.getConfigurer(SessionManagementConfigurer.class);
//        assertThat(configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
//
//        verify(httpSecurity.addFilterBefore(jwtAuthenticationFilter,UsernamePasswordAuthenticationFilter.class));
//
//        verify(httpSecurity.authorizeHttpRequests()).requestMatchers("/api/admin/**").hasAnyAuthority(UserRole.ADMIN.name());
//        verify(httpSecurity.authorizeHttpRequests()).requestMatchers("/api/customer/**").hasAnyAuthority(UserRole.CUSTOMER.name());
//        verify(httpSecurity.authorizeHttpRequests()).anyRequest().authenticated();
//
//
//    }

}


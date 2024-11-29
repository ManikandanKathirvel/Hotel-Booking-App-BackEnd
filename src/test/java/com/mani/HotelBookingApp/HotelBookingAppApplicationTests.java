package com.mani.HotelBookingApp;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class HotelBookingAppApplicationTests {

    @Test
    void contextLoads() {
        Logger logger= LoggerFactory.getLogger(HotelBookingAppApplicationTests.class);
        logger.info("contextLoad");
    }

}

package com.example.delivery;

import com.example.delivery.model.Weather;
import com.example.delivery.service.WeatherService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class WeatherServiceTests {
    @Autowired
    WeatherService weatherService;

    @Test
    public void testWeatherService() throws DeliveryException {
        weatherService.updateWeatherData();
        String station = weatherService.getStations()[0];
        Weather weather = weatherService.getWeatherByStation(station);
        assertEquals(station, weather.getStation());
    }
}

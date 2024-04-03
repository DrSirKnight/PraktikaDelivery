package com.example.delivery.controller;

import com.example.delivery.model.City;
import com.example.delivery.model.Weather;
import com.example.delivery.service.CityService;
import com.example.delivery.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
public class Controller {
    private String city = "";
    private String vehicle = "";
    private final WeatherService weatherService;
    private final CityService cityService;
    private String errorMessage = "";
    private LocalDateTime findTime;

    @Autowired
    public Controller(WeatherService weatherService, CityService cityService) {
        this.weatherService = weatherService;
        this.cityService = cityService;
    }

    /**
     * Calculates the total fee depending on the current vehicle and city. Requires vehicle and city to be set.
     * @return The calculated fee or -1 if exception was thrown
     */
    @GetMapping("/api/getFee")
    public double getFee() {
        try {
            double totalFee = 0;
            City currentCity = cityService.getCity(city);
            totalFee += currentCity.getRBF(vehicle);
            Weather cityWeather;
            if (findTime != null) {
                cityWeather = weatherService.getWeatherByStationAndTime(currentCity.getWeatherStation(), findTime);
                findTime = null;
            } else {
                cityWeather = weatherService.getWeatherByStation(currentCity.getWeatherStation());
            }
            totalFee += cityWeather.getATEF(vehicle);
            totalFee += cityWeather.getWPEF(vehicle);
            totalFee += cityWeather.getWSEF(vehicle);
            vehicle = "";
            city = "";
            return totalFee;
        } catch (Exception e) {
            setErrorMessage(e.getMessage());
            return -1;
        }
    }

    /**
     * Gets the error message if an exception was thrown
     * @return The thrown exceptions message
     */
    @GetMapping("/api/error")
    public String getErrorMsg() {
        return errorMessage;
    }

    /**
     * Sets the city for fee calculation
     * @param city What the city will be set to
     */
    @PostMapping("/api/setCity")
    public void setCity(@RequestBody String city) {
        this.city = city.toLowerCase();
    }

    /**
     * Sets the vehicle for fee calculation
     * @param vehicle What the vehicle will be set to
     */
    @PostMapping("api/setVehicle")
    public void setVehicle(@RequestBody String vehicle) {
        this.vehicle = vehicle.toLowerCase();
    }

    /**
     * Sets a timestamp to get
     * @param time
     */
    @PostMapping("api/setTime")
    public void setTime(@RequestBody String time) {
        this.findTime = LocalDateTime.parse(time);
    }

    private void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}

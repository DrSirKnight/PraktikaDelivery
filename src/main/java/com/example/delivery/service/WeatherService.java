package com.example.delivery.service;

import com.example.delivery.DeliveryException;
import com.example.delivery.model.Weather;
import com.example.delivery.repository.WeatherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class WeatherService {
    private final WeatherRepository repository;
    private final WebService web;

    private final String[] stations = {"Tallinn-Harku", "Tartu-Tõravere", "Pärnu"};

    @Autowired
    public WeatherService(WeatherRepository repository, WebService web) {
        this.repository = repository;
        this.web = web;
        try {
            updateWeatherData();
        } catch (Exception e) {
            System.out.println("Unable to update weather data, thrown exception: " + e.getMessage());
        }
    }

    public Weather getWeatherByStation(String station) {
        return repository.findByLatestStation(station);
    }

    public Weather getWeatherByStationAndTime(String station, LocalDateTime time){return repository.findByTimeStation(station, Timestamp.valueOf(time));}

    @Scheduled(cron = "0 15 * * * ?")
    public void updateWeatherData() throws DeliveryException {
        List<Weather> weatherList = web.getWebData(stations, "https://www.ilmateenistus.ee/ilma_andmed/xml/observations.php");
        repository.saveAll(weatherList);
    }

    public String[] getStations() {
        return stations;
    }
}

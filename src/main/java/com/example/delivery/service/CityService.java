package com.example.delivery.service;

import com.example.delivery.model.City;
import com.example.delivery.DeliveryException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class CityService {
    List<City> cityList;

    /**
     * City service Constructor
     * Adds all the cities to an array
     */
    CityService() {
        cityList = new ArrayList<>();
        cityList.add(makeNewCity("tallinn", "Tallinn-Harku", 4.0, 3.5, 3.0));
        cityList.add(makeNewCity("tartu", "Tartu-Tõravere", 3.5, 3.0, 2.5));
        cityList.add(makeNewCity("pärnu", "Pärnu", 3.0, 2.5, 2.0));
    }

    private City makeNewCity(String name, String weatherStation, double carValue, double scooterValue, double bikeValue) {
        HashMap<String, Double> vehicles = new HashMap<>();
        vehicles.put("car", carValue);
        vehicles.put("scooter", scooterValue);
        vehicles.put("bike", bikeValue);
        return new City( name, weatherStation, vehicles);
    }

    /**
     * Gets the city with given name
     * @param cityName The name of the city
     * @return The city with the given name
     * @throws DeliveryException Thrown with message 'Invalid city name' if city is not found
     */
    public City getCity(String cityName) throws DeliveryException {
        for (City city : cityList) {
            if (city.getName().equals(cityName)) {
                return city;
            }
        }
        throw new DeliveryException("Invalid city name");
    }

    /**
     * Gets a list of all cities
     * @return The list containing all cities
     */
    public List<City> getCityList() {
        return cityList;
    }
}

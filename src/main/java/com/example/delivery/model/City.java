package com.example.delivery.model;

import java.util.HashMap;

public class City {
    private final String name;
    private final String weatherStation;
    private final HashMap<String, Double> vehicleFees;

    /**
     * City constructor
     * @param name Name of the city
     * @param weatherStation The cities weather station
     * @param vehicleFees Hashmap of vehicles and the fees assigned to them. Vehicle names are the key.
     */
    public City(String name, String weatherStation, HashMap<String, Double> vehicleFees) {
        this.name = name;
        this.weatherStation = weatherStation;
        this.vehicleFees = vehicleFees;
    }

    /**
     * Gets the fee for the given vehicle name
     * @param vehicle Given vehicle's name
     * @return The fee for that vehicle
     */
    public double getRBF(String vehicle) {
        return vehicleFees.get(vehicle.toLowerCase());
    }

    /**
     * Gets the name of the city
     * @return The cities name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the weather station of the city
     * @return The weather stations name
     */
    public String getWeatherStation() {
        return weatherStation;
    }
}

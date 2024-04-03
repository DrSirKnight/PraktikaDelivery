package com.example.delivery.model;

import com.example.delivery.DeliveryException;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;

@Entity
public class Weather {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String station;
    private int WMO;
    private double airTemp;
    private double windSpeed;
    private String phenomenon;
    private LocalDateTime timestamp;

    /**
     * Constructor for weather
     * @param station The weather station
     * @param WMO WMO code of the weather station
     * @param airTemp Current air temperature
     * @param windSpeed Current wind speed
     * @param phenomenon Current phenomenon in the local area
     * @param timestamp Timestamp of this weather information
     */
    public Weather(String station, int WMO, double airTemp, double windSpeed, String phenomenon, LocalDateTime timestamp) {
        this.station = station;
        this.WMO = WMO;
        this.airTemp = airTemp;
        this.windSpeed = windSpeed;
        this.phenomenon = phenomenon.toLowerCase();
        this.timestamp = timestamp;
    }

    public Weather() {
    }

    /**
     * Calculates extra fees based on the current wind speed
     * @param vehicle The type of vehicle used for delivery
     * @return Total fee based on weather condition
     * @throws Exception DeliveryException if a vehicle is not allowed to be used in the current weather conditions
     */
    public double getWSEF(String vehicle) throws DeliveryException {
        if (vehicle.equalsIgnoreCase("bike")) {
            // Wind speed
            if (windSpeed > 20) {
                throw new DeliveryException("Usage of selected vehicle type is forbidden");
            } else if (windSpeed > 10) {
                return 0.5;
            }
        }
        return 0;
    }

    /**
     * Calculates extra fees based on the current air temperature
     * @param vehicle The type of vehicle used for delivery
     * @return Total fee based on weather condition
     */
    public double getATEF(String vehicle) {
        if (vehicle.equalsIgnoreCase("scooter") || vehicle.equalsIgnoreCase("bike")) {
            if (airTemp < -10) {
                return 1;
            } else if (airTemp < 0) {
                return 0.5;
            }
        }
        return 0;
    }

    /**
     * Calculates extra fees based on the current weather phenomenon
     * @param vehicle The type of vehicle used for delivery
     * @return Total fee based on weather condition
     * @throws DeliveryException Throws a Delivery Exception if a vehicle is not allowed to be used in the current weather conditions
     */
    public double getWPEF(String vehicle) throws DeliveryException {
        if (vehicle.equalsIgnoreCase("scooter") || vehicle.equalsIgnoreCase("bike")) {
            if (phenomenon.contains("snow") || phenomenon.contains("sleet")) {
                return 1;
            } else if (phenomenon.contains("rain")) {
                return 0.5;
            } else if (phenomenon.contains("glaze") || phenomenon.contains("hail") || phenomenon.contains("thunder")) {
                throw new DeliveryException("Usage of selected vehicle type is forbidden");
            }
        }
        return 0;
    }

    /**
     * Gets the station
     * @return The current weather's station
     */
    public String getStation() {
        return station;
    }

    /**
     * Gets the timestamp
     * @return The current weathers creation timestamp
     */
    public LocalDateTime getTimestamp() {return timestamp;}
}

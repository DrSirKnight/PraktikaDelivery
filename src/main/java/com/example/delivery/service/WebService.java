package com.example.delivery.service;

import com.example.delivery.DeliveryException;
import com.example.delivery.model.Weather;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.net.URI;
import java.net.URLConnection;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class WebService {
    /**
     * Gets data from the web about given weather stations
     * @param stationNames Array of station names, that will have their data fetched
     * @param webAddress The URL to get data from
     * @return List containing weather objects from given stations with information gathered from the given URL
     */
    public List<Weather> getWebData(String[] stationNames, String webAddress) throws DeliveryException {
        try {
            // Connecting to the website
            URI uri = new URI(webAddress);
            URLConnection connection = uri.toURL().openConnection();

            // Making a document using the data from the website
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(connection.getInputStream());

            // The document is split into stations which are added to a list
            NodeList stations = doc.getElementsByTagName("station");

            // List of weathers that'll eventually be returned
            ArrayList<Weather> weatherList = new ArrayList<>();

            // Current time, used for setting timestamp, will be the same for every weather
            LocalDateTime localDate = LocalDateTime.now();
            localDate = LocalDateTime.of(localDate.getYear(), localDate.getMonth(), localDate.getDayOfMonth(), localDate.getHour(), localDate.getMinute(), localDate.getSecond());

            // Loop that goes through every station
            for (int i = 0; i < stations.getLength(); i++) {
                Node station = stations.item(i);
                if (station.getNodeType() == Node.ELEMENT_NODE) {
                    Element stationElement = (Element) station;
                    // Get the stations name and check if it's the station we're looking for
                    String stationName = stationElement.getElementsByTagName("name").item(0).getTextContent();
                    if (Arrays.asList(stationNames).contains(stationName)) {
                        // Make a weather object with the stations info
                        String phenomenon = stationElement.getElementsByTagName("phenomenon").item(0).getTextContent();
                        String wmoCodeStr = stationElement.getElementsByTagName("wmocode").item(0).getTextContent();
                        String airTemperatureStr = stationElement.getElementsByTagName("airtemperature").item(0).getTextContent();
                        String windSpeedStr = stationElement.getElementsByTagName("windspeed").item(0).getTextContent();
                        // Checks if the WMO code is empty, if it is, we'll just set it to 0
                        int wmoCode = 0;
                        if (!wmoCodeStr.isEmpty()) {
                            wmoCode = Integer.parseInt(wmoCodeStr);
                        }
                        // Checks if air temperature or wind speed are empty and sets it to 0 if it is
                        double airTemperature = strToDouble(airTemperatureStr, "Air Temperature");
                        double windSpeed = strToDouble(windSpeedStr, "Wind Speed");
                        // Add the final weather object to our array
                        weatherList.add(new Weather(stationName, wmoCode, airTemperature, windSpeed, phenomenon, localDate));
                    }
                }
            }
            return weatherList;
        } catch (Exception e) {
            throw new DeliveryException("Unable to fetch data from website, error message: \n" + e.getMessage());
        }
    }

    private double strToDouble(String str, String type) throws DeliveryException {
        double num;
        try {
            num = Double.parseDouble(str);
        } catch (NumberFormatException nfe) {
            num = 0;
        }
        return num;
    }
}

package com.example.delivery;

import com.example.delivery.controller.Controller;
import com.example.delivery.model.Weather;
import com.example.delivery.service.CityService;
import com.example.delivery.service.WeatherService;
import com.example.delivery.service.WebService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest
class ControllerTests {

	@Autowired
	private WebService webService;
	@MockBean
	private WeatherService weatherService;
	@Autowired
	private CityService cityService;

	@Autowired
	private Controller controller;

	@BeforeEach
	public void setup() {
		controller = new Controller(weatherService, cityService);
	}

	@Test
	void contextLoads() {
	}

	@Test
    public void testFeeBadWeatherBike() throws Exception {
		when(weatherService.getWeatherByStation("Tartu-Tõravere")).thenReturn(new Weather("Tartu-Tõravere", 26242, -2.1, 14.7, "Light snow shower", LocalDateTime.now()));
		controller.setCity("Tartu");
		controller.setVehicle("Bike");

		assertEquals(4.5, controller.getFee());
	}

	@Test
	public void testFeeGoodWeatherBike() throws Exception {
		when(weatherService.getWeatherByStation("Tartu-Tõravere")).thenReturn(new Weather("Tartu-Tõravere", 26242, 10.2, 0.7, "", LocalDateTime.now()));
		controller.setCity("Tartu");
		controller.setVehicle("Bike");

		assertEquals(2.5, controller.getFee());
	}

	@Test
	public void testFeeExtremeWeatherBike() throws Exception {
		when(weatherService.getWeatherByStation("Tartu-Tõravere")).thenReturn(new Weather("Tartu-Tõravere", 26242, 10.2, 20.7, "", LocalDateTime.now()));
		controller.setCity("Tartu");
		controller.setVehicle("Bike");
		controller.getFee();

		assertEquals("Usage of selected vehicle type is forbidden", controller.getErrorMsg());
	}

	@Test
	public void testFeeExtremeWeatherBike2() {
		when(weatherService.getWeatherByStation("Tartu-Tõravere")).thenReturn(new Weather("Tartu-Tõravere", 26242, 10.2, 0.7, "Thunder", LocalDateTime.now()));
		controller.setCity("Tartu");
		controller.setVehicle("Bike");
		controller.getFee();

		assertEquals("Usage of selected vehicle type is forbidden", controller.getErrorMsg());
	}

	@Test
	public void testInvalidCityName() {
		controller.setCity("Narva");
		controller.getFee();
		assertEquals("Invalid city name", controller.getErrorMsg());
	}

	@Test
	public void testWebService() throws DeliveryException {
		List<Weather> weatherList = webService.getWebData(new String[]{"Tallinn-Harku"}, "https://www.ilmateenistus.ee/ilma_andmed/xml/observations.php");
		assertEquals("Tallinn-Harku", weatherList.getFirst().getStation());
	}

	@Test
	public void testWebServiceMissingData() throws DeliveryException {
		List<Weather> weatherList = webService.getWebData(new String[]{"Kassari"}, "https://www.ilmateenistus.ee/ilma_andmed/xml/observations.php");

		assertEquals(0, weatherList.get(0).getWMO());
	}

	@Test
	public void testFeeBadWeatherScooter() throws Exception {
		when(weatherService.getWeatherByStation("Tartu-Tõravere")).thenReturn(new Weather("Tartu-Tõravere", 26242, -10.2, 20.7, "Rain", LocalDateTime.now()));
		controller.setCity("Tartu");
		controller.setVehicle("Scooter");

		assertEquals(4.5, controller.getFee());
	}

	@Test
	public void testCar() throws Exception {
		when(weatherService.getWeatherByStation("Tartu-Tõravere")).thenReturn(new Weather("Tartu-Tõravere", 26242, -10.2, 20.7, "Rain", LocalDateTime.now()));
		controller.setCity("Tartu");
		controller.setVehicle("Car");

		assertEquals(3.5, controller.getFee());
	}

	@Test
	public void testFeeBadWeatherBike2() throws Exception {
		when(weatherService.getWeatherByStation("Tartu-Tõravere")).thenReturn(new Weather("Tartu-Tõravere", 26242, -2.1, 14.7, "Sleet", LocalDateTime.now()));
		controller.setCity("Tartu");
		controller.setVehicle("Bike");

		assertEquals(4.5, controller.getFee());
	}

	@Test
	public void testFeeBadWeatherBike3() throws Exception {
		when(weatherService.getWeatherByStation("Tartu-Tõravere")).thenReturn(new Weather("Tartu-Tõravere", 26242, -2.1, 14.7, "Glaze", LocalDateTime.now()));
		controller.setCity("Tartu");
		controller.setVehicle("Bike");
		controller.getFee();

		assertEquals("Usage of selected vehicle type is forbidden", controller.getErrorMsg());
	}

	@Test
	public void testFeeBadWeatherBike4() throws Exception {
		when(weatherService.getWeatherByStation("Tartu-Tõravere")).thenReturn(new Weather("Tartu-Tõravere", 26242, -2.1, 14.7, "Hail", LocalDateTime.now()));
		controller.setCity("Tartu");
		controller.setVehicle("Bike");
		controller.getFee();

		assertEquals("Usage of selected vehicle type is forbidden", controller.getErrorMsg());
	}
}

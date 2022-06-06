package com.udacity.vehicles.api;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.udacity.vehicles.client.maps.MapsClient;
import com.udacity.vehicles.client.prices.PriceClient;
import com.udacity.vehicles.domain.Condition;
import com.udacity.vehicles.domain.Location;
import com.udacity.vehicles.domain.car.Car;
import com.udacity.vehicles.domain.car.Details;
import com.udacity.vehicles.domain.manufacturer.Manufacturer;
import com.udacity.vehicles.service.CarService;
import java.net.URI;
import java.util.Collections;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Implements testing of the CarController class.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
public class CarControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private JacksonTester<Car> json;

    @MockBean
    private CarService carService;

    @MockBean
    private PriceClient priceClient;

    @MockBean
    private MapsClient mapsClient;

    /**
     * Creates pre-requisites for testing, such as an example car.
     */
    @Before
    public void setup() {
        Car car = getCar();
        car.setId(1L);
        given(carService.save(any())).willReturn(car);
        given(carService.findById(any())).willReturn(car);
        given(carService.list()).willReturn(Collections.singletonList(car));
    }

    /**
     * Tests for successful creation of new car in the system
     * @throws Exception when car creation fails in the system
     */
    @Test
    public void createCar() throws Exception {
        Car car = getCar();
        mvc.perform(
                post(new URI("/cars"))
                        .content(json.write(car).getJson())
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isCreated());
    }

    /**
     * Tests if the read operation appropriately returns a list of vehicles.
     * @throws Exception if the read operation of the vehicle list fails
     */
    @Test
    public void listCars() throws Exception {
        var expectedJson = "{\"_embedded\":{\"carList\":[{\n" +
                "   \"condition\":\"USED\",\n" +
                "   \"details\":{\n" +
                "      \"body\":\"sedan\",\n" +
                "      \"model\":\"Impala\",\n" +
                "      \"manufacturer\":{\n" +
                "         \"code\":101,\n" +
                "         \"name\":\"Chevrolet\"\n" +
                "      },\n" +
                "      \"numberOfDoors\":4,\n" +
                "      \"fuelType\":\"Gasoline\",\n" +
                "      \"engine\":\"3.6L V6\",\n" +
                "      \"mileage\":32280,\n" +
                "      \"modelYear\":2018,\n" +
                "      \"productionYear\":2018,\n" +
                "      \"externalColor\":\"white\"\n" +
                "   },\n" +
                "   \"location\":{\n" +
                "      \"lat\":40.73061,\n" +
                "      \"lon\":-73.935242\n" +
                "   }\n" +
                "}]}}";


        mvc.perform(
                        get(new URI("/cars"))
                                .contentType(MediaType.APPLICATION_JSON_UTF8)
                                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));

    }

    /**
     * Tests the read operation for a single car by ID.
     * @throws Exception if the read operation for a single car fails
     */
    @Test
    public void findCar() throws Exception {
        var expectedJson = "{\n" +
                "   \"condition\":\"USED\",\n" +
                "   \"details\":{\n" +
                "      \"body\":\"sedan\",\n" +
                "      \"model\":\"Impala\",\n" +
                "      \"manufacturer\":{\n" +
                "         \"code\":101,\n" +
                "         \"name\":\"Chevrolet\"\n" +
                "      },\n" +
                "      \"numberOfDoors\":4,\n" +
                "      \"fuelType\":\"Gasoline\",\n" +
                "      \"engine\":\"3.6L V6\",\n" +
                "      \"mileage\":32280,\n" +
                "      \"modelYear\":2018,\n" +
                "      \"productionYear\":2018,\n" +
                "      \"externalColor\":\"white\"\n" +
                "   },\n" +
                "   \"location\":{\n" +
                "      \"lat\":40.73061,\n" +
                "      \"lon\":-73.935242\n" +
                "   }\n" +
                "}";


        mvc.perform(
                        get(new URI("/cars/1"))
                                .contentType(MediaType.APPLICATION_JSON_UTF8)
                                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));
    }

    /**
     * Tests the update operation for a single car by ID.
     * @throws Exception if the read operation for a single car fails
     */
    @Test
    public void updateCar() throws Exception {
        Car newCar = carService.findById(1L);
        newCar.setId(1L);
        Details details = newCar.getDetails();
        details.setMileage(32792);
        details.setExternalColor("gray");
        newCar.setDetails(details);
        newCar.setLocation(new Location(42.6519, -73.7761));
        var expectedJsonNewCar = "{\n" +
                "   \"condition\":\"USED\",\n" +
                "   \"details\":{\n" +
                "      \"body\":\"sedan\",\n" +
                "      \"model\":\"Impala\",\n" +
                "      \"manufacturer\":{\n" +
                "         \"code\":101,\n" +
                "         \"name\":\"Chevrolet\"\n" +
                "      },\n" +
                "      \"numberOfDoors\":4,\n" +
                "      \"fuelType\":\"Gasoline\",\n" +
                "      \"engine\":\"3.6L V6\",\n" +
                "      \"mileage\":32792,\n" +
                "      \"modelYear\":2018,\n" +
                "      \"productionYear\":2018,\n" +
                "      \"externalColor\":\"gray\"\n" +
                "   },\n" +
                "   \"location\":{\n" +
                "      \"lat\":42.6519,\n" +
                "      \"lon\":-73.7761\n" +
                "   }\n" +
                "}";


        mvc.perform(
                        put("/cars/1", 1L)
//                                .param("id", "1L")
//                                .param("car", json.write(newCar).getJson())
                                .content(json.write(newCar).getJson())
                                .contentType(MediaType.APPLICATION_JSON_UTF8)
                                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJsonNewCar));

    }

    /**
     * Tests the deletion of a single car by ID.
     * @throws Exception if the delete operation of a vehicle fails
     */
    @Test
    public void deleteCar() throws Exception {
        mvc.perform(
                        delete(new URI("/cars/1"))
                                .contentType(MediaType.APPLICATION_JSON_UTF8)
                                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNoContent());
    }

    /**
     * Creates an example Car object for use in testing.
     * @return an example Car object
     */
    private Car getCar() {
        Car car = new Car();
        car.setLocation(new Location(40.730610, -73.935242));
        Details details = new Details();
        Manufacturer manufacturer = new Manufacturer(101, "Chevrolet");
        details.setManufacturer(manufacturer);
        details.setModel("Impala");
        details.setMileage(32280);
        details.setExternalColor("white");
        details.setBody("sedan");
        details.setEngine("3.6L V6");
        details.setFuelType("Gasoline");
        details.setModelYear(2018);
        details.setProductionYear(2018);
        details.setNumberOfDoors(4);
        car.setDetails(details);
        car.setCondition(Condition.USED);
        return car;
    }
}
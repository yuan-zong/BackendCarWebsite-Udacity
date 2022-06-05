package com.udacity.pricing;

import com.udacity.pricing.entity.Price;
import org.junit.jupiter.api.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;
import java.math.BigDecimal;


//@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class PricingServiceApplicationTests {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;
	@Test
	public void contextLoads() {
	}

	@Test
	public void testGetAPrice() {
		long vehicleID = 1L;
		Price price = restTemplate.getForObject("http://localhost:" + port + "/prices/" + vehicleID, Price.class );
		Assertions.assertNotNull(price);
		Assertions.assertEquals(price.getCurrency(), "EUR");
		Assertions.assertEquals(price.getPrice(), new BigDecimal(3563));
	}

}

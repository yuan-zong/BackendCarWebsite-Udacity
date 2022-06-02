package com.udacity.pricing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class PricingMicroserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(PricingMicroserviceApplication.class, args);
    }

}

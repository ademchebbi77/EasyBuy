package com.esprit.microservice.apigatewaydemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableDiscoveryClient
public class ApiGatewayDemoApplication {

  public static void main(String[] args) {
    SpringApplication.run(ApiGatewayDemoApplication.class, args);
  }

  @Bean
  public RouteLocator gatewayroutes(RouteLocatorBuilder builder) {
    return builder.routes()
            .route("user-service-route",
                    r -> r.path("/api/users/**")
                            .uri("lb://USER-SERVICE"))
            .route("order-service-route",
                    r -> r.path("/api/orders/**")
                            .uri("lb://ORDER-SERVICE"))
            .route("product-service-route",
                    r -> r.path("/api/products/**")
                            .uri("lb://PRODUCT-SERVICE"))
            .build();
  }
}
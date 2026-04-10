package tn.esprit.spring.commande;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = "tn.esprit.spring")
@EnableJpaRepositories(basePackages = "tn.esprit.spring.backend.repository")
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "tn.esprit.spring.backend.client")
public class OrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class, args);
    }

}

package org.example;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {"db.service", "db.repository", "models", "utils", "managers", "server"})
@EnableJpaRepositories(basePackages = {"db.repository", "utils", "models", "server"})
@EntityScan(basePackages = "models")
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}

// TODO: Переделать UpdateID
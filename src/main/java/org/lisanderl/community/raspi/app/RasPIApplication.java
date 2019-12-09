package org.lisanderl.community.raspi.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.telegram.telegrambots.ApiContextInitializer;

@SpringBootApplication
@EnableScheduling
@ComponentScan({"org.lisanderl.community.raspi"})
public class RasPIApplication {

    public static void main(String[] args) {
        ApiContextInitializer.init();
        SpringApplication.run(RasPIApplication.class, args);
    }
}

package fitnesstracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class FitnessTrackerApplication {
    public static void main(String[] args) {
        SpringApplication.run(FitnessTrackerApplication.class, args);
    }
}

package api;

import api.Models.User;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories
@SpringBootApplication
@PropertySource("classpath:application.properties")
public class Main {
    public static User currentUser = null;
    public static ApplicationContext context;
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

}

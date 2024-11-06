package onedollarbid;

import onedollarbid.model.User;
import onedollarbid.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main implements CommandLineRunner {

    @Autowired
    private UserService userService;

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        User newUser = new User();
        newUser.setUsername("john_doe");
        newUser.setBalance(100.0);

        User createdUser = userService.save(newUser);
        System.out.println("Created User: " + createdUser);

        User updatedUser = new User();
        updatedUser.setUsername("john_doe");
        updatedUser.setBalance(150.0);
        User savedUser = userService.updateUser(createdUser.getId(), updatedUser);
        System.out.println("Updated User Balance: " + savedUser.getBalance());

        System.out.println("User deleted successfully");
    }
}

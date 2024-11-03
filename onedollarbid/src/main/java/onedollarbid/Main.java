package onedollarbid;

import org.apache.el.stream.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import onedollarbid.model.User;
import onedollarbid.repository.UserRepository;
import onedollarbid.service.UserService;

@SpringBootApplication
public class Main {
    @Autowired
    static final private UserService userService = new UserService();

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);

        // Create a new User
        User newUser = new User();
        newUser.setUsername("john_doe");
        newUser.setBalance(100.0);
        User createdUser = userService.save(newUser);
        System.out.println("Created User: " + createdUser);

        // Retrieve a User by ID

        // Update the User’s balance
        User updatedUser = new User();
        updatedUser.setUsername("john_doe");
        updatedUser.setBalance(150.0);
        User savedUser = userService.updateUser(createdUser.getId(), updatedUser);
        System.out.println("Updated User Balance: " + savedUser.getBalance());

        // Delete the User by ID
        userService.deleteById(savedUser.getId());
        System.out.println("User deleted successfully");
    }
}

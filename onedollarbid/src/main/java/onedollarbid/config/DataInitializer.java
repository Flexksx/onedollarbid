package onedollarbid.config;

import jakarta.annotation.PostConstruct;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import onedollarbid.user.User;
import onedollarbid.user.UserService;

import java.util.HashSet;
import java.util.Set;

@Component
public class DataInitializer {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void initData() {
        // Create admin user if it doesn't exist
        if (userService.findByUsername("admin").isEmpty()) {
            User adminUser = new User("admin", passwordEncoder.encode("admin"));
            Set<String> adminRoles = new HashSet<>();
            adminRoles.add("ADMIN");
            adminUser.setRoles(adminRoles);
            userService.save(adminUser);
        }

        // Create regular user if it doesn't exist
        if (userService.findByUsername("user").isEmpty()) {
            User regularUser = new User("user", passwordEncoder.encode("user"));
            Set<String> userRoles = new HashSet<>();
            userRoles.add("USER");
            regularUser.setRoles(userRoles);
            userService.save(regularUser);
        }
    }
}

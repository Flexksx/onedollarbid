package onedollarbid.auth;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import onedollarbid.security.JwtUtil;
import onedollarbid.security.SecurityUser;
import onedollarbid.security.UserDetailsServiceImpl;
import onedollarbid.user.User;
import onedollarbid.user.UserService;

import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Authentication API")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsServiceImpl userDetailsService;
    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public AuthController(AuthenticationManager authenticationManager, UserDetailsServiceImpl userDetailsService,
            JwtUtil jwtUtil, UserService userService, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    @Operation(summary = "User login", description = "Authenticate user and generate JWT token")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthRequest authRequest) throws Exception {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));

            final UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getUsername());
            final String jwt = jwtUtil.generateToken(userDetails);

            return ResponseEntity.ok(new AuthResponse(jwt));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(401).body("Incorrect username or password");
        }
    }

    @PostMapping("/register")
    @Operation(summary = "User registration", description = "Register a new user")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest registerRequest) {
        if (userService.findByUsername(registerRequest.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("Username already taken");
        }

        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));

        Set<String> roles = new HashSet<>();
        roles.add(registerRequest.getRole() != null ? registerRequest.getRole() : "USER");
        user.setRoles(roles);

        User savedUser = userService.save(user);

        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/token")
    @Operation(summary = "Generate token with specific role", description = "Generate JWT token with specific role for testing")
    public ResponseEntity<?> generateToken(@RequestBody TokenRequest tokenRequest) {
        if (tokenRequest.getUsername() == null || tokenRequest.getRole() == null) {
            return ResponseEntity.badRequest().body("Username and role are required");
        }

        User user = userService.findByUsername(tokenRequest.getUsername()).orElse(null);
        if (user == null) {
            // Create a new user with the specified role for demo purposes
            user = new User();
            user.setUsername(tokenRequest.getUsername());
            user.setPassword(passwordEncoder.encode("password"));

            Set<String> roles = new HashSet<>();
            roles.add(tokenRequest.getRole());
            user.setRoles(roles);

            user = userService.save(user);
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
        final String jwt = jwtUtil.generateToken(userDetails);

        return ResponseEntity.ok(new AuthResponse(jwt));
    }
}

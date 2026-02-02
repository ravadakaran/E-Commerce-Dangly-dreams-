package com.ecommerce.jewelleryMart.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.jewelleryMart.config.JwtUtil;
import com.ecommerce.jewelleryMart.model.User;
import com.ecommerce.jewelleryMart.repository.UserRepository;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    // ================= SIGNUP =================
    @PostMapping("/signup")
    public ResponseEntity<Map<String, Object>> registerUser(@RequestBody User user) {
        Map<String, Object> response = new HashMap<>();

        if (userRepository.existsByEmail(user.getEmail())) {
            response.put("error", "Email already registered.");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }

        if (user.getName() == null && user.getUsername() != null) {
            user.setName(user.getUsername());
        }
        if (user.getUsername() == null && user.getName() != null) {
            user.setUsername(user.getName());
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        response.put("message", "User registered successfully!");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // ================= LOGIN =================
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody User loginRequest) {
        Optional<User> userOptional = userRepository.findByEmail(loginRequest.getEmail());

        if (userOptional.isPresent()
                && passwordEncoder.matches(
                        loginRequest.getPassword(),
                        userOptional.get().getPassword())) {

            User user = userOptional.get();
            String role = user.getRole() != null ? user.getRole() : (user.isAdmin() ? "ADMIN" : "USER");
            String accessToken = jwtUtil.generateAccessToken(user.getId(), user.getEmail(), role);
            String refreshToken = jwtUtil.generateRefreshToken(user.getEmail());

            Map<String, Object> resp = new HashMap<>();
            resp.put("email", user.getEmail());
            resp.put("username", user.getUsername());
            resp.put("name", user.getName() != null ? user.getName() : user.getUsername());
            resp.put("userId", user.getId());
            resp.put("isAdmin", user.isAdmin());
            resp.put("role", role);
            resp.put("accessToken", accessToken);
            resp.put("refreshToken", refreshToken);
            resp.put("expiresIn", 86400); // seconds

            return ResponseEntity.ok(resp);
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", "Invalid email or password"));
    }

    // ================= REFRESH TOKEN =================
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody Map<String, String> body) {
        String refreshToken = body.get("refreshToken");
        if (refreshToken == null || !jwtUtil.validateToken(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid refresh token"));
        }
        String email = jwtUtil.getEmailFromToken(refreshToken);
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "User not found"));
        }
        User user = userOptional.get();
        String role = user.getRole() != null ? user.getRole() : (user.isAdmin() ? "ADMIN" : "USER");
        String newAccessToken = jwtUtil.generateAccessToken(user.getId(), user.getEmail(), role);
        return ResponseEntity.ok(Map.of(
                "accessToken", newAccessToken,
                "expiresIn", 86400
        ));
    }

    // ================= GET USER =================
    @GetMapping("/user")
    public ResponseEntity<?> getUserByEmail(@RequestParam String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setPassword(null); // never expose password
            return ResponseEntity.ok(user);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", "User not found"));
    }

    // ================= UPDATE USER =================
    @PutMapping("/user")
    public ResponseEntity<?> updateUser(
            @RequestParam String email,
            @RequestBody User updatedData) {

        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "User not found"));
        }

        User user = userOptional.get();
        if (updatedData.getUsername() != null) user.setUsername(updatedData.getUsername());
        if (updatedData.getName() != null) user.setName(updatedData.getName());
        userRepository.save(user);

        user.setPassword(null);
        return ResponseEntity.ok(user);
    }

    // ================= RESET PASSWORD =================
    @PutMapping("/reset-password")
    public ResponseEntity<Map<String, String>> resetPassword(
            @RequestBody Map<String, String> requestBody) {

        String email = requestBody.get("email");
        String newPassword = requestBody.get("newPassword");

        if (email == null || newPassword == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Email and new password are required."));
        }

        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "User not found."));
        }

        User user = userOptional.get();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        return ResponseEntity.ok(
                Map.of("message", "Password updated successfully."));
    }
}

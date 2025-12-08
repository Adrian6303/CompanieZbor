package com.example.companiezbor.rest;

import com.example.companiezbor.model.User;
import com.example.companiezbor.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService service;

    public UserController(UserService service) { this.service = service; }

    @GetMapping
    public List<User> all() { return service.findAll(); }

    @GetMapping("/{id}")
    public ResponseEntity<User> get(@PathVariable Integer id) {
        return service.findById(id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<User> create(@RequestBody User user) {
        User saved = service.save(user);
        return ResponseEntity.created(URI.create("/api/users/" + saved.getId())).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> update(@PathVariable Integer id, @RequestBody User user) {
        return service.findById(id).map(existing -> {
            existing.setUsername(user.getUsername());
            if (user.getPassword() != null) existing.setPassword(user.getPassword());
            existing.setAdmin(user.isAdmin());
            User updated = service.save(existing);
            return ResponseEntity.ok(updated);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        if (service.findById(id).isPresent()) {
            service.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    //enter username and password in post-body than receive user if exists
    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody User user) {
        return service.findAll().stream()
                .filter(u -> u.getUsername().equals(user.getUsername()) && u.getPassword().equals(user.getPassword()))
                .findFirst()
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(401).build());
    }
}

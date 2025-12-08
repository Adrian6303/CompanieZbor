package com.example.companiezbor.service;

import com.example.companiezbor.model.User;
import org.springframework.stereotype.Service;
import com.example.companiezbor.repository.UserRepository;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository repo;

    public UserService(UserRepository repo) {
        this.repo = repo;
    }

    public List<User> findAll() { return repo.findAll(); }
    public Optional<User> findById(Integer id) { return repo.findById(id); }
    public User save(User user) { return repo.save(user); }
    public void deleteById(Integer id) { repo.deleteById(id); }
}

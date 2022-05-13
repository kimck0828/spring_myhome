package com.example.myhome.controller;

import com.example.myhome.model.Board;
import com.example.myhome.model.User;
import com.example.myhome.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.StringUtils;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UserApiController {
    
    @Autowired
    private UserRepository userRepository;
    
    @GetMapping("/users")
    public List<User> getAll(){
        return userRepository.findAll();
    }
    
    @GetMapping("/users/{id}")
    public User getUser(@PathVariable Long id) {
        return userRepository.findById(id).orElse(null);
    }
    
    @PostMapping("/users")
    public User newUser(@RequestBody User user) {
        return userRepository.save(user);
    }
    
    @PutMapping("/users/{id}")
    public User replaceUser(@RequestBody User newUser, @PathVariable Long id) {
        return userRepository.findById(id)
                .map(user -> {
                    for ( Board board : newUser.getBoards() ) {
                        board.setUser(user);
                    }
                    user.setBoards(newUser.getBoards());
                    
                    return userRepository.save(user);
                })
                .orElseGet(() ->{
                    newUser.setId(id);
                    return userRepository.save(newUser);
                });
    }
    
    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);
    }
    
}

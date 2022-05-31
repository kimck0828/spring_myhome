package com.example.myhome.controller;

import com.example.myhome.config.WebSecurityConfig;
import com.example.myhome.model.Board;
import com.example.myhome.model.User;
import com.example.myhome.repository.UserRepository;
import com.example.myhome.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.mariadb.jdbc.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
// SLF4Jログライブラリを使用する（LOMBOKの機能）
@Slf4j
public class UserApiController {
    
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    
    
    @GetMapping("/users")
    public List<User> getAll(){
        List<User> users = userRepository.findAll();
        log.debug(">>>呼び出し前");
        log.debug(">>>サイズ：{}", users.get(0).getBoards().size());
        log.debug(">>>呼び出し後");
        return users;
    }
    
    @GetMapping("/users/{id}")
    public User getUser(@PathVariable Long id) {
        return userRepository.findById(id).orElse(null);
    }
    
    @PostMapping("/users")
    public User newUser(@RequestBody User user) {
//        if (user.getPassword() != null) {
//            user.setPassword(passwordEncoder.encode(user.getPassword()));
//        }
        return userService.save(user);
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
    
    @Secured("ROLE_ADMIN") // ロール「ROLE_ADMIN」のみ実行可能
    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);
    }
    
}

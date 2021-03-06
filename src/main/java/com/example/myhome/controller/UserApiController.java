package com.example.myhome.controller;

import com.example.myhome.model.Board;
import com.example.myhome.model.QUser;
import com.example.myhome.model.User;
import com.example.myhome.repository.UserRepository;
import com.example.myhome.service.UserService;
import com.querydsl.core.types.Predicate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
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
    public Iterable<User> getAll(@RequestParam(required = false) String method,
                             @RequestParam(required = false) String text){
        Iterable<User> users = null;
        if ( "query".equals(method) ) {
            users = userRepository.findByUsernameQuery(text);
        }
        else if ( "native".equals(method) ) {
            users = userRepository.findByUsernameNativeQuery(text);
        }
        else if ( "querydsl".equals(method) ) {
            QUser user = QUser.user;
            
            // textが含まれている＋BOARDテーブルにデータがない
//            Predicate predicate = user.username.contains(text)
//                    .isTrue().and(user.boards.isNotEmpty());
            Predicate predicate = user.username.contains(text);
            users = userRepository.findAll(predicate);
        }
        else if ( "querydslCustom".equals(method) ) {
            users = userRepository.findByUsernameNativeQuery(text);
        }
        else if ( "querydslCustomJdbc".equals(method) ) {
            users = userRepository.findByUsernameCustomJdbc(text);
        }
        else {
            users = userRepository.findAll();
        }
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

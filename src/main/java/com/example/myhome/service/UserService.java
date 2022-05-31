package com.example.myhome.service;

import com.example.myhome.model.Role;
import com.example.myhome.model.User;
import com.example.myhome.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    public User save(User user) {
        // ★APIからも実行させるために修正
        // パスワード暗号化
        if ( user.getPassword() != null ) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        // 使用有無（デフォルト）
        if ( user.getEnabled() == null ) {
            user.setEnabled(true);
        }
        // ロール（デフォルト）
        if ( user.getRoles().isEmpty() ) {
            Role role = new Role();
            role.setId(1L);
            user.getRoles().add(role);
        }
        return userRepository.save(user);
    }
}

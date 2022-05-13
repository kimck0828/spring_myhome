package com.example.myhome.repository;

import com.example.myhome.model.Board;
import com.example.myhome.model.User;
import com.example.myhome.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    
    User findByUsername(String username);
}

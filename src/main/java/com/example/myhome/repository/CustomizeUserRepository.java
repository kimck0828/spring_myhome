package com.example.myhome.repository;

import com.example.myhome.model.User;

import java.util.List;

public interface CustomizeUserRepository {
    List<User> findByUsernameCustom(String username);
    List<User> findByUsernameCustomJdbc(String username);
}

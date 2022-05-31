package com.example.myhome.repository;

import com.example.myhome.model.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    // BOARDテーブル情報取得時にJOINして取得するための設定。UserクラスでBOARDテーブル情報になる変数名
    @EntityGraph(attributePaths = "boards")
    List<User> findAll();

    @Override
    @EntityGraph(attributePaths = "boards")
    Optional<User> findById(Long id);

    User findByUsername(String username);
}

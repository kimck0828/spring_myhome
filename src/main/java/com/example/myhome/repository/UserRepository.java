package com.example.myhome.repository;

import com.example.myhome.model.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, QuerydslPredicateExecutor<User>, CustomizeUserRepository {
    // BOARDテーブル情報取得時にJOINして取得するための設定。UserクラスでBOARDテーブル情報になる変数名
    @EntityGraph(attributePaths = "boards")
    List<User> findAll();

    @Override
    @EntityGraph(attributePaths = "boards")
    Optional<User> findById(Long id);

    User findByUsername(String username);

    @Query("select u from User u where u.username like %?1%")
    List<User> findByUsernameQuery(String username);

    @Query(value = "select * from User where username like %?1%", nativeQuery = true)
    List<User> findByUsernameNativeQuery(String username);
}

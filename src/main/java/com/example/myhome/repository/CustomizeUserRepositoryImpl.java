package com.example.myhome.repository;

import com.example.myhome.model.QUser;
import com.example.myhome.model.User;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.Types;
import java.util.List;

public class CustomizeUserRepositoryImpl implements CustomizeUserRepository{
    @PersistenceContext
    private EntityManager em;
    @Autowired
    JdbcTemplate jdbcTemplate;
    
    @Override
    public List<User> findByUsernameCustom(String username) {

        QUser qUser = QUser.user;
        JPAQuery<?> query = new JPAQuery<Void>(em);
        
        List<User> users = query.select(qUser)
                .from(qUser)
                .where(qUser.username.contains(username))
                .fetch();
        
        return users;
        
    }

    @Override
    public List<User> findByUsernameCustomJdbc(String username) {
        String sql = "select * from user where username like ?";
        
        List<User> users = jdbcTemplate.query(
                sql, new Object[]{"%" + username + "%"}, new int[]{Types.VARCHAR}, new BeanPropertyRowMapper<>(User.class));
        
        return users;
    }
}

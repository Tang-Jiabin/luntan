package com.example.luntan.dao;

import com.example.luntan.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {

    Optional<User> findByOpenid(String openid);

    @Query(nativeQuery = true,value = "SELECT count(*) FROM luntan_user")
    Integer findCount();
}

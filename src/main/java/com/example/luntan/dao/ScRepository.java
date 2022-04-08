package com.example.luntan.dao;

import com.example.luntan.pojo.Sc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Repository
public interface ScRepository extends JpaRepository<Sc,Integer> {
    List<Sc> findAllByUid(Integer uid);

    Optional<Sc> findByUidAndFid(Integer uid, Integer id);
}

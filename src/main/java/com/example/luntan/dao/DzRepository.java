package com.example.luntan.dao;

import com.example.luntan.pojo.Dz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DzRepository extends JpaRepository<Dz,Integer> {
    List<Dz> findAllByUid(Integer uid);

    Optional<Dz> findByUidAndFid(Integer uid, Integer id);
}

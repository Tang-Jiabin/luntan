package com.example.luntan.dao;

import com.example.luntan.pojo.Jl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JlRepository extends JpaRepository<Jl,Integer> {
    Optional<Jl> findByFidAndUid(Integer fid, Integer uid);

    List<Jl> findAllByUid(Integer loginId);
}

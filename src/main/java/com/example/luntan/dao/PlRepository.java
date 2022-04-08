package com.example.luntan.dao;

import com.example.luntan.pojo.Pl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlRepository extends JpaRepository<Pl,Integer> , JpaSpecificationExecutor<Pl> {

    Optional<Pl> findFirstByFidAndPidOrderByStorey(Integer fid,Integer pid);
    Optional<Pl> findFirstByFidAndPidOrderByStoreyDesc(Integer fid,Integer pid);
}

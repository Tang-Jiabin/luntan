package com.example.luntan.dao;

import com.example.luntan.pojo.Pl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlRepository extends JpaRepository<Pl,Integer> , JpaSpecificationExecutor<Pl> {

    Optional<Pl> findFirstByFidAndPidOrderByStorey(Integer fid,Integer pid);
    Optional<Pl> findFirstByFidAndPidOrderByStoreyDesc(Integer fid,Integer pid);

    @Query(nativeQuery = true,value = "SELECT count(*) from l_pl where uid = ?1")
    Integer findCountByUid(Integer uid);
}

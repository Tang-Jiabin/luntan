package com.example.luntan.dao;

import com.example.luntan.pojo.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report,Integer>, JpaSpecificationExecutor<Report> {
    List<Report> findAllByUid(Integer uid);

    List<Report> findAllByFid(Integer id);
}

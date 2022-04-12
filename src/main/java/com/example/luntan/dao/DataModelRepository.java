package com.example.luntan.dao;

import com.example.luntan.pojo.DataModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DataModelRepository extends JpaRepository<DataModel, Integer> {

    Optional<DataModel> findByUidAndAndFid(Integer uid, Integer fid);
}

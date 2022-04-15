package com.example.luntan.dao;

import com.example.luntan.pojo.Forum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ForumRepository extends JpaRepository<Forum,Integer>, JpaSpecificationExecutor<Forum> {

    @Query(nativeQuery = true,value = "SELECT count(*) FROM luntan_forum WHERE uid = ?1")
    Integer findCountByUid(Integer uid);

    @Query(nativeQuery = true,value = "SELECT count(*) FROM luntan_forum")
    Integer findCount();
}

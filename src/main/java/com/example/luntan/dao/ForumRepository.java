package com.example.luntan.dao;

import com.example.luntan.pojo.Forum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ForumRepository extends JpaRepository<Forum,Integer>, JpaSpecificationExecutor<Forum> {
}

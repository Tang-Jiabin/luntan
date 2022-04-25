package com.example.luntan;


import com.example.luntan.dao.ForumRepository;
import com.example.luntan.dao.PlRepository;
import com.example.luntan.pojo.Forum;
import com.example.luntan.pojo.Pl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class LuntanApplicationTests {

    @Autowired
    private ForumRepository forumRepository;
    @Autowired
    private PlRepository plRepository;

    @Test
    void test(){
        List<Forum> forumList = forumRepository.findAll();
        List<Pl> plList = plRepository.findAll();
        for (Forum forum : forumList) {
            int count = 0;
            for (Pl pl : plList) {
                if (forum.getId().equals(pl.getFid())){
                    count++;
                }
            }
            forum.setPlmun(count);
            Forum save = forumRepository.save(forum);
            System.out.println(save);
        }
    }


}

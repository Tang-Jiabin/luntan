package com.example.luntan;

import com.example.luntan.dao.ForumRepository;
import com.example.luntan.dao.PlRepository;
import com.example.luntan.dao.UserRepository;
import com.example.luntan.pojo.Pl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;


@SpringBootTest
class LuntanApplicationTests {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ForumRepository forumRepository;
    @Autowired
    private PlRepository plRepository;

    @Test
    void test() throws InterruptedException {
        List<Pl> all = plRepository.findAll();
        for (Pl pl : all) {
            int length = pl.getContent().length();
            if (length>5){
                pl.setContent(pl.getContent().substring(0,5));
                plRepository.save(pl);
            }
        }
    }




}

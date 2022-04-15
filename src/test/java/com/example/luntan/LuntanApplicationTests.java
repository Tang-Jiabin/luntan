package com.example.luntan;

import java.time.Instant;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.listener.PageReadListener;
import com.alibaba.fastjson.JSON;
import com.example.luntan.dao.ForumRepository;
import com.example.luntan.pojo.DataModel;
import com.example.luntan.pojo.Forum;
import com.example.luntan.service.Recommend;
import com.example.luntan.service.Similarity;
import com.example.luntan.service.impl.GenericUserBasedRecommender;
import com.example.luntan.service.impl.PearsonCorrelationSimilarity;
import com.example.luntan.util.DemoData;


import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;


@SpringBootTest
class LuntanApplicationTests {


    @Autowired
    private ForumRepository forumRepository;

    @Test
    void contextLoads() {
        List<Forum> all = forumRepository.findAll();
        List<Forum> forums = new ArrayList<>();
        for (Forum forum : all) {
            if (forum.getLabelsid().equals(0)) {
                String content = forum.getContent();
                int labelId = getLabelId(content);
                forum.setLabelsid(labelId);
                forums.add(forum);
            }
        }

        forumRepository.saveAll(forums);

    }


    public static void main(String[] args) throws Exception {


    }

    @Test
    void importExl() {

        String fileName = "C:/Users/jymj3/Desktop/百度.xlsx";
        List<Forum> forums = new ArrayList<>();

        EasyExcel.read(fileName, DemoData.class, new PageReadListener<DemoData>(dataList -> {
            for (DemoData demoData : dataList) {

                if (demoData.getYuan() == null) {
                    continue;
                }
                if (demoData.getYuan().contains("大学") || demoData.getYuan().contains("学院") || demoData.getYuan().contains("校区")) {
                    String content = demoData.getTitle() + demoData.getContent();
//                int labelId = getLabelId(content);
                    int labelId = 0;
                    if (demoData.getType().contains("租房")) {
                        labelId = 4;
                    }
                    if (demoData.getType().contains("交友")) {
                        labelId = 5;
                    }
                    if (demoData.getType().contains("失物")) {
                        labelId = 6;
                    }
                    if (demoData.getType().contains("求职")) {
                        labelId = 7;
                    }
                    if (demoData.getType().contains("交流")) {
                        labelId = 8;
                    }
                    if (demoData.getType().contains("报考")) {
                        labelId = 9;
                    }
                    if (demoData.getType().contains("生活")) {
                        labelId = 10;
                    }
                    if (demoData.getType().contains("吐槽")) {
                        labelId = 11;
                    }
                    Forum forum = new Forum();
                    forum.setUid(0);
                    forum.setLabelsid(labelId);
                    forum.setCtime(Instant.now());
                    forum.setUtime(Instant.now());
                    forum.setContent(demoData.getTitle() + demoData.getContent());
                    forum.setPicture("");
                    forum.setDzmun(0);
                    forum.setPlmun(0);
                    forum.setScmun(0);
                    forums.add(forum);

                    System.out.println("读取到一条数据 " + JSON.toJSONString(demoData));
                }

            }
        })).sheet().doRead();

        System.out.println(forums.size());
        forumRepository.saveAll(forums);


    }

    public static void getTie() throws Exception {
        OkHttpClient httpClient = new OkHttpClient().newBuilder().build();
        Request.Builder builder = new Request.Builder();
        Request request = builder.url("https://tieba.baidu.com/f/search/res?ie=utf-8&kw=%E5%8C%97%E4%BA%AC%E5%BB%BA%E7%AD%91%E5%B7%A5%E7%A8%8B%E5%AD%A6%E9%99%A2&qw=%E9%97%B2%E7%BD%AE").build();
        Response response = httpClient.newCall(request).execute();
        System.out.println(response.body().string());
    }

    public static void re(List<DataModel> dataModelList) {
        //计算相似度
        Similarity similarity = new PearsonCorrelationSimilarity(dataModelList);
        //构建基于用户的推荐系统
        Recommend recommend = new GenericUserBasedRecommender(dataModelList, similarity);
        //构建基于物品的推荐系统
//        Recommend recommend = new GenericItemBasedRecommender(dataModelList, similarity);
        //推荐
        List<Integer> list = recommend.recommendedBecause(1, 1, 10);

        list.forEach(System.out::println);
    }

    private static int getLabelId(String content) {
        int labelId = 0;
        if (content.contains("租") || content.contains("借") || content.contains("售") || content.contains("求购")) {
            labelId = 4;
        }
        if (content.contains("交") || content.contains("友") || content.contains("认识") || content.contains("找")) {
            labelId = 5;
        }
        if (content.contains("丢") || content.contains("失") || content.contains("帮") || content.contains("联系")) {
            labelId = 6;
        }
        if (content.contains("招") || content.contains("岗") || content.contains("实习") || content.contains("公司") || content.contains("职") || content.contains("家教") || content.contains("辅导") || content.contains("补") || content.contains("老师") || content.contains("偿") || content.contains("赚")) {
            labelId = 7;
        }
        if (content.contains("资料") || content.contains("学习") || content.contains("教程") || content.contains("册") || content.contains("纸")) {
            labelId = 8;
        }
        if (content.contains("考") || content.contains("专业") || content.contains("试") || content.contains("批") || content.contains("分")) {
            labelId = 9;
        }
        if (content.contains("活") || content.contains("学校") || content.contains("课") || content.contains("新生")) {
            labelId = 10;
        }
        if (content.contains("看看") || content.contains("快来") || content.contains("各位") || content.contains("散布") || content.contains("打击") || content.contains("传说") || content.contains("骗") || content.contains("差") || content.contains("服务") || content.contains("姐") || content.contains("玩") || content.contains("偷")) {
            labelId = 11;
        }
        return labelId;
    }

}

package com.example.luntan.re;

import com.example.luntan.pojo.DataModel;
import com.example.luntan.service.Recommend;
import com.example.luntan.service.Similarity;
import com.example.luntan.service.impl.GenericItemBasedRecommender;
import com.example.luntan.service.impl.GenericUserBasedRecommender;
import com.example.luntan.service.impl.PearsonCorrelationSimilarity;

import java.util.*;

public class Recommend2 {


    public static void main(String[] args) {

        //数据模型
        List<DataModel> dataModelList = new ArrayList<>();
        DataModel dataModel = new DataModel();
        dataModel.setId(1);
        dataModel.setUid(1);
        dataModel.setFid(1);
        dataModel.setScore(302);
        dataModelList.add(dataModel);
        dataModel = new DataModel();
        dataModel.setId(2);
        dataModel.setUid(1);
        dataModel.setFid(2);
        dataModel.setScore(253);
        dataModelList.add(dataModel);
        dataModel = new DataModel();
        dataModel.setId(3);
        dataModel.setUid(1);
        dataModel.setFid(3);
        dataModel.setScore(300);
        dataModelList.add(dataModel);
        dataModel = new DataModel();
        dataModel.setId(4);
        dataModel.setUid(2);
        dataModel.setFid(1);
        dataModel.setScore(302);
        dataModelList.add(dataModel);
        dataModel = new DataModel();
        dataModel.setId(5);
        dataModel.setUid(2);
        dataModel.setFid(2);
        dataModel.setScore(253);
        dataModelList.add(dataModel);
        dataModel = new DataModel();
        dataModel.setId(6);
        dataModel.setUid(2);
        dataModel.setFid(3);
        dataModel.setScore(310);
        dataModelList.add(dataModel);
        dataModel = new DataModel();
        dataModel.setId(6);
        dataModel.setUid(2);
        dataModel.setFid(4);
        dataModel.setScore(210);
        dataModelList.add(dataModel);

        dataModel = new DataModel();
        dataModel.setId(7);
        dataModel.setUid(3);
        dataModel.setFid(1);
        dataModel.setScore(300);
        dataModelList.add(dataModel);
        dataModel = new DataModel();
        dataModel.setId(8);
        dataModel.setUid(3);
        dataModel.setFid(2);
        dataModel.setScore(250);
        dataModelList.add(dataModel);
        dataModel = new DataModel();
        dataModel.setId(9);
        dataModel.setUid(3);
        dataModel.setFid(3);
        dataModel.setScore(290);
        dataModelList.add(dataModel);

        dataModelList.forEach(System.out::println);
        re(dataModelList);

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

//    /**
//     * 在给定username的情况下，计算其他用户和它的距离并排序
//     *
//     * @param username
//     * @param set
//     * @return
//     */
//    private Map<Double, String> computeNearestNeighbor(String username, DataModel dataModel) {
//        Map<Double, String> distances = new TreeMap<>();
//
//        UserSet.User u1 = set.getUser(username);
//        for (int i = 0; i < set.users.size(); i++) {
//            UserSet.User u2 = set.getUser(i);
//
//            if (!u2.username.equals(username)) {
//                double distance = pearson_dis(u2.list, u1.list);
//                distances.put(distance, u2.username);
//            }
//
//        }
//        System.out.println("distance => " + distances);
//        return distances;
//    }
//
//
//    /**
//     * 计算2个打分序列间的pearson距离
//     *
//     * @param rating1
//     * @param rating2
//     * @return
//     */
//    private double pearson_dis(List<UserSet.Set> rating1, List<UserSet.Set> rating2) {
//        int sum_xy = 0;
//        int sum_x = 0;
//        int sum_y = 0;
//        double sum_x2 = 0;
//        double sum_y2 = 0;
//        int n = 0;
//        for (int i = 0; i < rating1.size(); i++) {
//            UserSet.Set key1 = rating1.get(i);
//            for (int j = 0; j < rating2.size(); j++) {
//                UserSet.Set key2 = rating2.get(j);
//                if (key1.username.equals(key2.username)) {
//                    n += 1;
//                    int x = key1.score;
//                    int y = key2.score;
//                    sum_xy += x * y;
//                    sum_x += x;
//                    sum_y += y;
//                    sum_x2 += Math.pow(x, 2);
//                    sum_y2 += Math.pow(y, 2);
//                }
//
//            }
//        }
//        double denominator = Math.sqrt(sum_x2 - Math.pow(sum_x, 2) / n) * Math.sqrt(sum_y2 - Math.pow(sum_y, 2) / n);
//        if (denominator == 0) {
//            return 0;
//        } else {
//            return (sum_xy - (sum_x * sum_y) / n) / denominator;
//        }
//    }
//
//
//    public List<UserSet.Set> recommend(String username, UserSet set) {
//        //找到最近邻
//        Map<Double, String> distances = computeNearestNeighbor(username, set);
//        String nearest = distances.values().iterator().next();
//        System.out.println("nearest -> " + nearest);
//
//
//        List<UserSet.Set> recommendations = new ArrayList<>();
//
//        //找到最近邻看过，但是我们没看过的电影，计算推荐
//        UserSet.User neighborRatings = set.getUser(nearest);
//        System.out.println("neighborRatings -> " + neighborRatings.list);
//
//        UserSet.User userRatings = set.getUser(username);
//        System.out.println("userRatings -> " + userRatings.list);
//
//        for (UserSet.Set artist : neighborRatings.list) {
//            if (userRatings.find(artist.username) == null) {
//                recommendations.add(artist);
//            }
//        }
//        Collections.sort(recommendations);
//        System.out.println("recommendations -> " + recommendations.toString());
//        return recommendations;
//    }
}

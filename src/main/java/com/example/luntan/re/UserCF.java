package com.example.luntan.re;

import java.util.*;
import java.util.Map.Entry;

public class UserCF {

    public static void main(String[] args) {
        /**
         * 知识点参考：https://blog.csdn.net/qq_36254699/article/details/105160984
         * 输入用户-->车辆条目  一个用户对应多个车辆
         * 用户ID	车辆ID集合
         */
        System.out.println(System.getProperty("file.encoding"));
        List<String> tempA = new ArrayList<>();
        tempA.add("a");
        tempA.add("b");
        tempA.add("d");
        List<String> tempB = new ArrayList<>();
        tempB.add("a");
        tempB.add("c");
        List<String> tempC = new ArrayList<>();
        tempC.add("b");
        List<String> tempD = new ArrayList<>();
        tempD.add("c");
        tempD.add("d");
        tempD.add("e");
        Map<String,List<String>> list_items = new HashMap<>();
        list_items.put("A",tempA);
        list_items.put("B",tempB);
        list_items.put("C",tempC);
        list_items.put("D",tempD);
        String target = "A";
        Map<String, Double> re = UserCF.run(list_items, target);
        for(Map.Entry<String, Double> entry : re.entrySet()){
            String mapKey = entry.getKey();
            Double mapValue = entry.getValue();
            System.out.println("为"+target+"推荐车辆ID："+mapKey+" 偏好度："+mapValue);
        }
    }


    public static Map<String, Double> run(Map<String,List<String>> list_item, String recommendUser) {
        System.out.println("接口传入内容："+list_item.toString());

        // 存储每一个用户对应的不同车辆总数  eg: A 3
        Map<String, Integer> userItemLength = new HashMap<>();
        // 存储车辆到用户的倒排表 eg: a A B
        Map<String, Set<String>> itemUserCollection = new HashMap<>();
        // 存储车辆集合 eg: a,b,c,d,e
        Set<String> items = new HashSet<>();
        // 存储每一个用户的购买车辆集合 eg: A: a,b,d
        Map<String, Set<String>> userPurchase = new HashMap<>();
        // 存储每一个用户的用户ID映射 eg: A:0  B:1
        Map<String, Integer> userID = new HashMap<>();
        // 存储每一个ID对应的用户映射 eg: 0:A  1:B
        Map<Integer, String> idUser = new HashMap<>();
        // 依次处理N个用户 以空格间隔
        int i=0;
        Iterator it = list_item.entrySet().iterator();
        while(it.hasNext()){
            Map.Entry entry = (Map.Entry) it.next();
            // entry.getKey().toString():用户ID
            // entry.getValue().toString():用户ID的购买记录
            System.out.println(entry.getKey() + " : " + entry.getValue());
            List<String> entryValue = (List<String>) entry.getValue();
            // 存储用户的购买记录
            String[] user_item = new String[entryValue.size()];
            entryValue.toArray(user_item);
            int length = user_item.length;
            //eg: A 3 代表A用户偏好三种车辆
            userItemLength.put(entry.getKey().toString(), length);
            //用户ID与稀疏矩阵建立对应关系
            userID.put(entry.getKey().toString(), i);
            idUser.put(i, entry.getKey().toString());
            //记录用户ID和购买记录关系存到userPurchase
            Set<String> temp = new HashSet<>();
            for (int j = 0; j < length; j++) {
                temp.add(user_item[j]);
            }
            userPurchase.put(entry.getKey().toString(),temp);
            // 建立车辆--用户倒排表
            for(int j = 0; j < length; j ++){
                //如果已经包含对应的车辆--用户映射，直接添加对应的用户
                if(items.contains(user_item[j])){
                    itemUserCollection.get(user_item[j]).add(entry.getKey().toString());
                }else{
                    //否则创建对应车辆--用户集合映射
                    items.add(user_item[j]);
                    //创建车辆--用户倒排关系
                    itemUserCollection.put(user_item[j], new HashSet<>());
                    itemUserCollection.get(user_item[j]).add(entry.getKey().toString());
                }
            }
            i++;
        }
        // 计算相似度矩阵【稀疏】
        // 存储用户两两之间的相似度,该变量仅仅记录分子部分（用于相似度矩阵计算）
        int[][] sparseMatrix = new int[list_item.size()][list_item.size()];
        Set<Entry<String, Set<String>>> entrySet = itemUserCollection.entrySet();
        Iterator<Entry<String, Set<String>>> iterator = entrySet.iterator();
        while(iterator.hasNext()){
            Set<String> commonUsers = iterator.next().getValue();
            for (String user_u : commonUsers) {
                for (String user_v : commonUsers) {
                    if(user_u.equals(user_v)){
                        continue;
                    }
                    //计算用户u与用户v都有正反馈的车辆总数
                    sparseMatrix[userID.get(user_u)][userID.get(user_v)] += 1;
                }
            }
        }
        // 计算用户之间的相似度【余弦相似性】
        // 根据用户获取存储的ID 比如被推送的用户是A，那么recommendUserId=0
        int recommendUserId = userID.get(recommendUser);
        // 存储所有相似用户ID
        Set<String> similarUsers = new HashSet<>();
        // 存储相似用户购买车辆集合
        Set<String> similarUsersItems = new HashSet<>();
        for (int j = 0;j < sparseMatrix.length; j++) {
            if(j != recommendUserId){
                // 目标用户和相似用户的相似度
                double semblance = sparseMatrix[recommendUserId][j]/Math.sqrt(userItemLength.get(recommendUser)*userItemLength.get(idUser.get(j)));
                if(semblance > 0){
                    similarUsers.add(idUser.get(j));
                    similarUsersItems.addAll(userPurchase.get(idUser.get(j)));
                }
            }
        }

        // 存储目标用户对车辆的偏好度并按偏好度降序
        Map<String,Double> result = new HashMap<>();
        // 计算指定用户recommendUser的车辆推荐度 从相似用户的购买记录中遍历每一件车辆
        // 遍历每一件车辆
        for(String item: similarUsersItems){
            //得到购买当前车辆的所有用户集合
            Set<String> users = itemUserCollection.get(item);
            //如果被推荐用户没有购买当前车辆，则进行推荐度计算
            if(!users.contains(recommendUser)){
                double itemRecommendDegree = 0.0;
                for(String user: users){
                    //推荐度计算
                    itemRecommendDegree += sparseMatrix[userID.get(recommendUser)][userID.get(user)]/Math.sqrt(userItemLength.get(recommendUser)*userItemLength.get(user));
                }
                result.put(item,itemRecommendDegree);
            }
        }
        return result;
    }


}

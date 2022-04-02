package com.example.luntan.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.luntan.common.RestResponse;
import okhttp3.*;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class OkHttpUtil {
    private static OkHttpClient okHttpClient = new OkHttpClient().newBuilder().retryOnConnectionFailure(true)
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS).build();

    private static final MediaType JSON_TYPE
            = MediaType.parse("application/json; charset=utf-8");

    /**
     * 发送post请求
     *
     * @param url
     * @param json
     * @return
     */
    public static JSONObject post(String url, String json) {
        RequestBody body = RequestBody.create(JSON_TYPE, json);
        Request request = new Request.Builder().url(url).post(body).build();
        try (Response response = okHttpClient.newCall(request).execute()) {
            ResponseBody responseBody = response.body();
            if (responseBody != null) {
                JSONObject jsonObject = JSONObject.parseObject(responseBody.string());
                jsonObject.put("code", 200);
                return jsonObject;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return RestResponse.error(406, "请求失败").toJSON();
    }

    public static JSONObject get(String url) {
        Request request = new Request.Builder()
                .url(url)
                .build();
        try (Response response = okHttpClient.newCall(request).execute()) {
            String body = response.body().string();
            JSONObject jsonObject = JSONObject.parseObject(body);
            jsonObject.put("code", 200);
            return jsonObject;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return RestResponse.error(406, "请求失败").toJSON();
    }
}

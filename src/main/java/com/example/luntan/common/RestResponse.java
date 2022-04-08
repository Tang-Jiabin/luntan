package com.example.luntan.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
@ApiModel(value = "RestResponse<T>", description = "响应通用参数包装")
public class RestResponse<T> {

    @ApiModelProperty("响应错误编码,0为正常")
    private int code;

    @ApiModelProperty("响应错误信息")
    private String msg;

    @ApiModelProperty("响应内容")
    private T data;

    private RestResponse() {

    }

    public static <T> RestResponse<T> success() {
        RestResponse<T> response = new RestResponse<T>();
        response.setCode(200);
        return response;
    }

    public static <T> RestResponse<T> success(T result) {
        RestResponse<T> response = new RestResponse<T>();
        response.setCode(200);
        response.setData(result);
        return response;
    }

    public static <T> RestResponse<T> error(int code, String msg) {
        RestResponse<T> response = new RestResponse<T>();
        response.setCode(code);
        response.setMsg(msg);
        return response;
    }


    public RestResponse(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

   public JSONObject toJSON(){
        return (JSONObject) JSONObject.toJSON(this);
   }

}

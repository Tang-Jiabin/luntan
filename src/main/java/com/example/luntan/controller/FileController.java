package com.example.luntan.controller;

import com.example.luntan.common.RestResponse;
import com.example.luntan.dto.FileDTO;
import com.example.luntan.util.FileUtil;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;


@Slf4j
@RestController
@RequestMapping("File")
public class FileController {

    @Value("${file.dir}")
    private String urlPath;

    @ApiOperation("上传")
    @ApiImplicitParam(name = "MultipartFile", value = "上传", required = true, dataTypeClass = MultipartFile.class, paramType = "body")
    @PostMapping(value = "/upload")
    public RestResponse<Object> upload(@RequestParam MultipartFile file) {
        try {
            String fileName = FileUtil.fileUpload(file);
            return RestResponse.success(urlPath + fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return RestResponse.error(400, "上传失败");
    }
}

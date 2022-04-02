package com.example.luntan.util;

import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class FileUtil {
    private static Set<String> typeSet = new HashSet<>();

    static {
        typeSet.add(".exe");
        typeSet.add(".bat");
        typeSet.add(".sh");
        //....
    }

    public static String fileUpload(MultipartFile file) throws Exception {
        //验证信息
        if (file == null) {
            throw new Exception("无文件上传");
        }


        //获取文件的全名
        String fileName = file.getOriginalFilename();

        //禁止上传exe bat sh脚本文件
        int index = fileName.lastIndexOf(".");
        String fileType = fileName.substring(index);
        if (typeSet.contains(fileType)) {

            throw new Exception("禁止上传可执行文件");
        }
        if (file.getSize() == 0) {

            throw new Exception("上传的文件为0kb，请检查是否上传正确");
        }
        //确定上传文件的根目录 如果不存在 创建目录
        File sourcePath = new File(ResourceUtils.getURL("jar:").getPath().replace("jar:", "fileupload") + File.separator);
        if (!sourcePath.exists()) {

            sourcePath.mkdirs();
        }


        String uuid = UUID.randomUUID().toString().replace("-", "");
        //重新定义上传文件的名称防止文件上传到文件夹出现重复文件
        String realFileName = uuid + fileType;
        //准备文件上传的全路径  磁盘路径地址+文件名称
        File upLoadFile = new File(sourcePath.getPath() + File.separator + realFileName);
        //上传文件

        try {
            System.out.println(upLoadFile.getPath());
            file.transferTo(upLoadFile);
        } catch (IOException e) {
            e.printStackTrace();

            throw new Exception("文件上传失败");
        }


        return realFileName;
    }
}

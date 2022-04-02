package com.example.luntan.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class FileDTO {

    private Integer id;

    private String name;

    private Integer sex;

    private List<MultipartFile> fileList;
}

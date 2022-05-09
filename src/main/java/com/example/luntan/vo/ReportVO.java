package com.example.luntan.vo;

import com.example.luntan.pojo.Report;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ReportVO extends Report {

    private String forumContent;

    private String nickname;
}

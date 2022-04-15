package com.example.luntan.util;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class DemoData {
    //    标题	标题链接	数量	threadlist_abs	作者_链接	作者	作者_链接1	作者2	回复	j_icon_slot_链接	标题1	d_badge_lv	内容	p_tail	时间	p_tail_txt
//    搜索关键词	标题	帖子网址	来源吧	发帖人	时间	本吧等级	帖子内容	楼层
    @ExcelProperty("搜索关键词")
    private String type;
    @ExcelProperty("标题")
    private String title;
    @ExcelProperty("帖子网址")
    private String titleLink;
    @ExcelProperty("来源吧")
    private String yuan;

    @ExcelProperty("帖子内容")
    private String content;
    @ExcelProperty("发帖人")
    private String author;



}

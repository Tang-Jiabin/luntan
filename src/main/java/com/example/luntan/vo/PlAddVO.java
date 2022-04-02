package com.example.luntan.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class PlAddVO extends ItemIdVO {
    private String content;
}

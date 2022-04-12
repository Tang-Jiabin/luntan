package com.example.luntan.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemMode {
    private Integer id;
    private Integer score;
    private List<UserMode> userModeList;
}

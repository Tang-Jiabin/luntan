package com.example.luntan.service;

import java.util.List;
import java.util.Set;

public interface Recommend {

    List<Integer> recommendedBecause(Integer id, Integer page, Integer limit);
}

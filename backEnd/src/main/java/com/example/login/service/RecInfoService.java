package com.example.login.service;

import com.example.login.dao.RecInfoRepository;
import com.example.login.domain.Category;
import com.example.login.domain.RecInfo;
import com.example.login.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecInfoService {
    @Autowired
    private RecInfoRepository recInfoRepository;
    public List<RecInfo> getRecInfoByCategory(Category category){
        List<RecInfo> recInfos = recInfoRepository.findByCategory(category.getCategory());
        return recInfos;
    }
}

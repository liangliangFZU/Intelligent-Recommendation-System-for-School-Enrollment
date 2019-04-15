package com.example.login.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.login.domain.Category;
import com.example.login.domain.RecInfo;
import com.example.login.service.RecInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@EnableAutoConfiguration
public class RecInfoController {
    @Autowired
    private RecInfoService recInfoService;

    @RequestMapping(value = "/RecInfo", method = RequestMethod.POST)
    String getRecInfo(@RequestBody Category category){
        List<RecInfo> recInfos = recInfoService.getRecInfoByCategory(category);
        return JSONArray.toJSONString(recInfos);
    }
}

package com.example.login.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.login.domain.Mail;
import com.example.login.domain.RecInfo;
import com.example.login.domain.User;

import com.example.login.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@EnableAutoConfiguration
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/userRegister", method = RequestMethod.POST)
    String userRegister(@RequestBody User user) {
        JSONObject result = new JSONObject();
        if(userService.register(user))
            result.put("result","true");
        else
            result.put("result","false");
        return result.toJSONString();
    }

    @RequestMapping(value = "/userLogin", method = RequestMethod.POST)
    String userLogin(@RequestBody User user) {
        JSONObject result = new JSONObject();
        List<RecInfo> recInfos;
        if (userService.verify(user)) {
            result.put("result","true");
            if((recInfos = userService.PushRecInfo(user)) != null) {
                return result.toJSONString()+ JSONArray.toJSONString(recInfos);
            }
        } else {
            result.put("result","false");
        }
        return result.toJSONString();
    }
    @RequestMapping(value = "/changeSwitch",method = RequestMethod.POST)
    void changeSwitch(@RequestBody Mail mail){
        userService.changeSwitch(mail.getMail());
    }
}

package com.example.login.service;

import com.example.login.dao.RecInfoRepository;
import com.example.login.domain.RecInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.login.dao.UserRepository;
import com.example.login.domain.User;
import org.springframework.util.DigestUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RecInfoRepository recInfoRepository;
    public boolean verify(User user) {
        if (userRepository.findByMailAndPassword(user.getMail(),DigestUtils.md5DigestAsHex(user.getPassword().getBytes())).isEmpty()) {
            return false;
        } else {
            return true;
        }
    }
    public Boolean register(User user) {
        if (userRepository.findByMail(user.getMail()).isEmpty()) {
            user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
            userRepository.save(user);
            return true;
        } else {
            return false;
        }
    }
    public User login(User user){
        List<User> user1 = userRepository.findByMailAndPassword(user.getMail(), DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
        return user1.get(0);
    }
    public void changeSwitch(String mail){
            User user = userRepository.findByMail(mail).get(0);
            if (user.isPush_switch()){
                user.setPush_switch(false);
            }
            else {
                user.setPush_switch(true);
            }
            userRepository.save(user);

    }
    public List<RecInfo> PushRecInfo(User user){
        if(user.isPush_switch()){
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/mm/dd");
            List<RecInfo> recInfoList = recInfoRepository.findByCategory(user.getCategory());
            if(!recInfoList.isEmpty()){
                for (RecInfo recInfo:recInfoList) {
                    if(recInfo.isCampus()){
                        try {
                            Date date = simpleDateFormat.parse(recInfo.getDate());
                            Calendar calendar1 = Calendar.getInstance(),calendar2 = Calendar.getInstance();
                            calendar1.setTime(date);
                            calendar1.add(Calendar.DAY_OF_MONTH,1);
                            if(calendar1 != calendar2)
                                calendar1.add(Calendar.DAY_OF_MONTH,1);
                                if (calendar1 != calendar2) recInfoList.remove(recInfo);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            if (!recInfoList.isEmpty()) return  recInfoList;
        }
        return null;
    }
}

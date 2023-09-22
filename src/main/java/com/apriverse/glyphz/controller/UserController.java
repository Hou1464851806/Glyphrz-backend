package com.apriverse.glyphz.controller;

import com.auth0.jwt.JWT;
import com.apriverse.glyphz.annotation.UserLoginToken;
import com.apriverse.glyphz.model.Config;
import com.apriverse.glyphz.model.User;
import com.apriverse.glyphz.repository.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    private final GlyphRepository glyphRepository;
    private final FontRepository fontRepository;
    private final UserRepository userRepository;
    private final ActivityRepository activityRepository;
    private final ConfigRepository configRepository;

    //构造器注入
    @Autowired
    UserController(GlyphRepository glyphRepository, FontRepository fontRepository, UserRepository userRepository, ActivityRepository activityRepository, ConfigRepository configRepository) {
        this.glyphRepository = glyphRepository;
        this.fontRepository = fontRepository;
        this.userRepository = userRepository;
        this.activityRepository = activityRepository;
        this.configRepository = configRepository;
    }

    // 更新用户信息
    @UserLoginToken
    @PostMapping("/user/update")
    public ResponseEntity<String> updateUser(@RequestBody User updateUser, HttpServletRequest request) {
        //获取Cookie
        Cookie[] cookies = request.getCookies();
        String token = cookies[0].getValue();
        String userName = JWT.decode(token).getAudience().get(0);
        //获取用户信息
        User user = userRepository.findByName(userName);
        System.out.println("用户信息更新中");
        if (user != null) {
            updateUser.setName(user.getName());
            updateUser.setId(user.getId());
            updateUser.setPassword(user.getPassword());
            User result = userRepository.save(updateUser);
            System.out.println(result);
        }
        return ResponseEntity.ok().build();
    }

    //保存用户设置至云端
    @UserLoginToken
    @PostMapping("/config/save")
    public ResponseEntity<String> saveConfig(@RequestBody Config configLocal, HttpServletRequest request) {
        System.out.println(configLocal);
        //获取Cookie
        Cookie[] cookies = request.getCookies();
        String token = cookies[0].getValue();
        String userName = JWT.decode(token).getAudience().get(0);
        //获取用户信息
        User user = userRepository.findByName(userName);
        System.out.println("设置保存中");
        if (user != null) {
            configLocal.setUserId(user.getId());
            Config result = configRepository.save(configLocal);
            System.out.println(result);
        }
        return ResponseEntity.ok().build();
    }
}

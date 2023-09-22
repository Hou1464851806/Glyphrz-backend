package com.apriverse.glyphz.controller;

import com.auth0.jwt.JWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.apriverse.glyphz.annotation.PassToken;
import com.apriverse.glyphz.annotation.UserLoginToken;
import com.apriverse.glyphz.entity.user.UserLoginResponse;
import com.apriverse.glyphz.model.Config;
import com.apriverse.glyphz.model.User;
import com.apriverse.glyphz.repository.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OuthController {
    private final GlyphRepository glyphRepository;
    private final FontRepository fontRepository;
    private final UserRepository userRepository;
    private final ActivityRepository activityRepository;
    private final ConfigRepository configRepository;
    ObjectMapper objectMapper = new ObjectMapper();

    //构造器注入
    @Autowired
    OuthController(GlyphRepository glyphRepository, FontRepository fontRepository, UserRepository userRepository, ActivityRepository activityRepository, ConfigRepository configRepository) {
        this.glyphRepository = glyphRepository;
        this.fontRepository = fontRepository;
        this.userRepository = userRepository;
        this.activityRepository = activityRepository;
        this.configRepository = configRepository;
    }

    //请求loginInfo: name, password
    //验证是否存在该用户，不存在就新建用户条目
    //存在则检验密码是否正确
    //鉴权通过，生成token并从Cookie中返回，并且返回用户信息
    //返回设置信息
    @PassToken
    @PostMapping("/user/login")
    public ResponseEntity<UserLoginResponse> UserLogin(@RequestBody User loginInfo, HttpServletResponse response) {
        UserLoginResponse userLoginResponse = new UserLoginResponse();
        //验证用户是否存在
        User userInfo = userRepository.findByName(loginInfo.getName());
        //用户不存在新建条目
        if (userInfo == null) {
            System.out.printf("%s 用户不存在，自动注册\n", loginInfo.getName());
            userInfo = userRepository.save(loginInfo);
        }
        //校验密码
        else if (!userInfo.getPassword().equals(loginInfo.getPassword())) {
            System.out.printf("%s 用户密码错误，重新登录\n", loginInfo.getName());
            return ResponseEntity.status(403).build();
        }
        //取出用户信息，装配实体
        else {
            System.out.printf("%s 用户信息：%s\n", userInfo.getName(), userInfo);
            userLoginResponse.setAvatar(userInfo.getAvatar());
            userLoginResponse.setSignature(userInfo.getSignature());
            //检验用户是否存在设置
            Config configCloud = configRepository.findByUserId(userInfo.getId());
            if (configCloud != null) {
                //装配设置信息
                userLoginResponse.setConfig(configCloud);
            }
        }
        //生成token
        String token = userInfo.getToken();
        System.out.printf("用户%s, token生成: %s\n", userInfo.getName(), token);
        //从Cookie返回
        Cookie tokenCookie = new Cookie("token", token);
        tokenCookie.setHttpOnly(true);
        tokenCookie.setPath("/");
        response.addCookie(tokenCookie);
        //返回200和用户信息
        System.out.printf("\033[31m用户登录响应: %s\n\033[0m", userLoginResponse);
        return ResponseEntity.ok().body(userLoginResponse);
    }

    //用户登出，删除Cookies，返回200
    //需要token验证通过
    @UserLoginToken
    @GetMapping("/user/logout")
    public ResponseEntity<String> UserLogout(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        String token = cookies[0].getValue();
        String userName = JWT.decode(token).getAudience().get(0);
        for (Cookie cookie : cookies) {
            cookie.setPath("/");
            cookie.setMaxAge(0);
            response.addCookie(cookie);
        }
        System.out.printf("用户%s，token已删除\n", userName);
        return ResponseEntity.status(200).build();
    }
}

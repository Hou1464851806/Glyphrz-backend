package com.glyphrz.glyphrize.controller;

import com.auth0.jwt.JWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.glyphrz.glyphrize.annotation.PassToken;
import com.glyphrz.glyphrize.annotation.UserLoginToken;
import com.glyphrz.glyphrize.entity.FontSync;
import com.glyphrz.glyphrize.entity.UserLoginResponse;
import com.glyphrz.glyphrize.entity.explore.ExploreFont;
import com.glyphrz.glyphrize.entity.explore.ExploreFontEntity;
import com.glyphrz.glyphrize.entity.explore.ExploreFontRequest;
import com.glyphrz.glyphrize.entity.explore.ExploreFontsEntity;
import com.glyphrz.glyphrize.model.*;
import com.glyphrz.glyphrize.repository.*;
import com.glyphrz.glyphrize.utils.Unicodes;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Optional;

@RestController
public class MainController {
    private final GlyphRepository glyphRepository;
    private final FontRepository fontRepository;
    private final UserRepository userRepository;
    private final ActivityRepository activityRepository;
    private final ConfigRepository configRepository;
    ObjectMapper objectMapper = new ObjectMapper();

    //构造器注入
    @Autowired
    MainController(GlyphRepository glyphRepository, FontRepository fontRepository, UserRepository userRepository, ActivityRepository activityRepository, ConfigRepository configRepository) {
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

    //获取指定字体的所有字形
    @PassToken
    @GetMapping("/explore/font/download")
    public ResponseEntity<ArrayList<Glyph>> FontDownload(@RequestParam long fontKey) {
        System.out.println(fontKey);
        ArrayList<Glyph> glyphs = glyphRepository.findAllByFontKey(fontKey);
        return ResponseEntity.ok().body(glyphs);
    }

    //获取广场推荐字体列表
    @PassToken
    @GetMapping("/explore/fonts")
    public ResponseEntity<ExploreFontsEntity> getRecommendFonts() {
        //实体(字体列表）
        ExploreFontsEntity exploreFonts = new ExploreFontsEntity();
        //单个字体（字体信息+字形列表）
        //字体列表
        ArrayList<ExploreFont> exploreFontArrayList = new ArrayList<>();
        //字形列表

        //查询被推荐字体列表
        ArrayList<Font> recommendFonts = fontRepository.findFontsByIsRecommend(1);
        System.out.println(recommendFonts);
        //装配
        //单个字体
        for (Font recommendFont : recommendFonts) {
            ExploreFont exploreFont = new ExploreFont();
            //填写字体信息
            exploreFont.setExploreFont(recommendFont);
            //按照用户id查询用户信息
            Optional<User> user = userRepository.findById(Integer.parseInt(String.valueOf(recommendFont.getUserId())));
            //填写字体中用户名字
            exploreFont.setUserName(user.get().getName());
            //获取简介的unicodes
            ArrayList<String> unicodes = Unicodes.String2Unicodes(recommendFont.getDescription());
            ArrayList<Glyph> glyphs = new ArrayList<>();
            for (String unicode : unicodes) {
                //获取相应字形
                Glyph glyph = glyphRepository.findByUnicodeAndFontKey(unicode, recommendFont.getFontKey());
                if (glyph != null) {
                    glyphs.add(glyph);
                }
            }
            //将字形列表填入单个字体
            exploreFont.setGlyphs(glyphs);
            //将单个字体填入字体列表
            exploreFontArrayList.add(exploreFont);
        }
        //装配实体
        exploreFonts.setExploreFonts(exploreFontArrayList);
        return ResponseEntity.ok().body(exploreFonts);
    }

    //获取广场推荐字体信息和预览字形
    @PassToken
    @PostMapping("/explore/font")
    public ResponseEntity<ExploreFontEntity> getRecommendFont(@RequestBody ExploreFontRequest request) {
        ExploreFontEntity exploreFontEntity = new ExploreFontEntity();
        ArrayList<Glyph> glyphs = new ArrayList<>();
        long fontKey = request.getFontKey();
        ArrayList<String> unicodes = Unicodes.String2Unicodes(request.getPreview());
        for (String unicode : unicodes) {
            Glyph glyph = glyphRepository.findByUnicodeAndFontKey(unicode, fontKey);
            if (glyph != null) {
                glyphs.add(glyph);
            }
        }
        exploreFontEntity.setGlyphs(glyphs);
        return ResponseEntity.ok().body(exploreFontEntity);
    }

    //获取用户的云端字体列表
    @UserLoginToken
    @GetMapping("/fonts")
    public ResponseEntity<ExploreFontsEntity> getFonts(HttpServletRequest request) {
        ExploreFontsEntity exploreFontsEntity = new ExploreFontsEntity();
        ArrayList<ExploreFont> exploreFonts = new ArrayList<>();
        //获取Cookie
        Cookie[] cookies = request.getCookies();
        String token = cookies[0].getValue();
        String userName = JWT.decode(token).getAudience().get(0);
        //获取用户信息
        User user = userRepository.findByName(userName);
        ArrayList<Font> fonts = new ArrayList<>();
        //根据用户信息检索该用户拥有的字体列表
        if (user != null) {
            fonts = fontRepository.findFontsByUserId(user.getId());
        }
        for (Font font : fonts) {
            ExploreFont exploreFont = new ExploreFont();
            //填写字体信息
            exploreFont.setExploreFont(font);
            //获取简介的unicodes
            ArrayList<String> unicodes = Unicodes.String2Unicodes(font.getDescription());
            ArrayList<Glyph> glyphs = new ArrayList<>();
            for (String unicode : unicodes) {
                //获取相应字形
                Glyph glyph = glyphRepository.findByUnicodeAndFontKey(unicode, font.getFontKey());
                if (glyph != null) {
                    glyphs.add(glyph);
                }
            }
            //将字形列表填入单个字体
            exploreFont.setGlyphs(glyphs);
            //将单个字体填入字体列表
            exploreFonts.add(exploreFont);
        }
        //装配实体
        exploreFontsEntity.setExploreFonts(exploreFonts);
        return ResponseEntity.ok().body(exploreFontsEntity);
    }

    //通过token获取用户信息，进行数据云同步
    @UserLoginToken
    @PostMapping("/font/sync")
    public ResponseEntity<FontSync> FontSync(HttpServletRequest request, @RequestBody FontSync fontSyncRequest) {
        long gbkCount, count;
        System.out.println(fontSyncRequest);
        FontSync fontSyncResponse = new FontSync();
        //通过token获取用户信息--------------------------------------------------------------------------------------------
        Cookie[] cookie = request.getCookies();
        String token = cookie[0].getValue();
        String userName = JWT.decode(token).getAudience().get(0);
        User user = userRepository.findByName(userName);
        System.out.printf("\033[31m%s 正在进行云同步\n\033[0m", userName);
        //装配用户信息
        fontSyncResponse.setUserName(userName);
        //获取本地数据
        Font fontLocal = fontSyncRequest.getFont();
        fontLocal.setUserId(user.getId());
        ArrayList<Glyph> glyphsLocal = fontSyncRequest.getGlyphs();
        ArrayList<Activity> activitiesLocal = fontSyncRequest.getActivities();
        System.out.println("本地数据已获取");
        //参数校验-------------------------------------------------------------------------------------------------------
        //检验是否是新建行为
        System.out.println("检验是否为新建行为");
        Font fontCloud = fontRepository.findFontByFontKey(fontLocal.getFontKey());
        //新建字体,字形和热力
        if (fontCloud == null) {
            System.out.println("新建字体");
            fontCloud = fontRepository.save(fontLocal);
            //设置fontKey
            System.out.println("新建字形");
            for (Glyph glyphLocal : glyphsLocal) {
                glyphLocal.setFontKey(fontCloud.getFontKey());
                Glyph result = glyphRepository.save(glyphLocal);
                System.out.printf("新建字形信息：%s\n", result);
            }
            System.out.println("新建热力值");
            for (Activity activityLocal : activitiesLocal) {
                activityLocal.setFontKey(fontCloud.getFontKey());
                Activity result = activityRepository.save(activityLocal);
                System.out.printf("新建热力值信息：%s\n", result);
            }
            //统计数据
            gbkCount = glyphRepository.countByIsGbkAndFontKey(1, fontCloud.getFontKey());
            count = glyphRepository.countByFontKey(fontCloud.getFontKey());
            fontCloud.setCount(count);
            fontCloud.setGbkCount(gbkCount);
            fontCloud = fontRepository.save(fontCloud);
            System.out.printf("新建字体信息：%s\n", fontCloud);
            //装配实体
            fontSyncResponse.setFont(fontCloud);
            fontSyncResponse.setActivities(activitiesLocal);
        }
        //不新建则是同步行为
        else {
            System.out.println("非新建，同步信息");
            //同步热力
            //获取云端更新的热力信息
            ArrayList<Activity> activitiesCloudNewer = new ArrayList<>();
            //校验时间并存储较新热力
            System.out.println("同步热力值");
            if (activitiesLocal.isEmpty()) {
                activitiesCloudNewer = activityRepository.findAllByDayGreaterThanEqualAndFontKey(fontCloud.getSyncTime() / 864000L, fontCloud.getFontKey());
            } else {
                for (Activity activityLocal : activitiesLocal) {
                    Activity activityCloud = activityRepository.findByDayAndFontKey(activityLocal.getDay(), fontCloud.getFontKey());
                    if (activityCloud == null) {
                        Activity result = activityRepository.save(activityLocal);
                        System.out.printf("新建未同步天数热力值：%s\n", result);
                        activitiesCloudNewer.add((result));
                    } else {
                        long newerActivity = activityCloud.getActivity() + activityLocal.getActivity();
                        activityCloud.setActivity(newerActivity);
                        Activity result = activityRepository.save(activityCloud);
                        System.out.printf("同步热力值：%s\n", result);
                        activitiesCloudNewer.add(activityCloud);
                    }
                }
            }
            //装配热力
            fontSyncResponse.setActivities(activitiesCloudNewer);

            //同步字形
            System.out.println("同步字形");
            //获取云端更新的字形信息
            ArrayList<Glyph> glyphsCloudNewer = glyphRepository.findAllByTimeGreaterThanAndFontKey(fontLocal.getSyncTime(), fontCloud.getFontKey());
            //校验时间并存储较新字形
            for (Glyph glyphLocal : glyphsLocal) {
                glyphLocal.setFontKey(fontCloud.getFontKey());
                Glyph glyphCloud = glyphRepository.findByUnicodeAndFontKey(glyphLocal.getUnicode(), fontCloud.getFontKey());
                if (glyphCloud == null) {
                    Glyph result = glyphRepository.save(glyphLocal);
                    System.out.printf("新建字形：%s\n", result);
                } else {
                    //相同字形存在更新版本则存储并且返回该更新版本
                    if (glyphLocal.getTime() >= glyphCloud.getTime()) {
                        Glyph result = glyphRepository.save(glyphLocal);
                        System.out.printf("字形%s已被替换为%s\n", glyphCloud, result);
                        glyphsCloudNewer.add(glyphLocal);
                    }
                    //相同字形的旧版本不存储被丢弃
                }
            }
            //装配字形信息
            System.out.println("从云端下载至本地字形：");
            System.out.println(glyphsCloudNewer);
            fontSyncResponse.setGlyphs(glyphsCloudNewer);

            //同步字体
            System.out.println("同步字体");
            //统计云端字数
            gbkCount = glyphRepository.countByIsGbkAndFontKey(1, fontCloud.getFontKey());
            count = glyphRepository.countByFontKey(fontCloud.getFontKey());
            //检验时间并存储较新字体信息，更新字数信息，并装配字体
            if (fontLocal.getInfoUpdateTime() >= fontCloud.getInfoUpdateTime()) {
                fontLocal.setCount(count);
                fontLocal.setGbkCount(gbkCount);
                fontRepository.save(fontLocal);
                fontSyncResponse.setFont(fontLocal);
            } else {
                fontCloud.setGbkCount(gbkCount);
                fontCloud.setCount(count);
                fontRepository.save(fontCloud);
                fontSyncResponse.setFont(fontCloud);
            }
            System.out.printf("字体信息：%s\n", fontSyncResponse.getFont());
        }

        //返回200和实体
        System.out.println(fontSyncResponse);
        return ResponseEntity.ok().body(fontSyncResponse);
    }


    //请求loginInfo: name, password
    //验证是否存在该用户，不存在就新建用户条目
    //存在则检验密码是否正确
    //鉴权通过，生成token并从Cookie中返回，并且返回用户信息。
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

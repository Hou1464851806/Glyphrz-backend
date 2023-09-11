package com.glyphrz.glyphrize.controller;

import com.auth0.jwt.JWT;
import com.glyphrz.glyphrize.annotation.UserLoginToken;
import com.glyphrz.glyphrize.entity.explore.ExploreFont;
import com.glyphrz.glyphrize.entity.explore.ExploreFontsEntity;
import com.glyphrz.glyphrize.entity.font.FontSync;
import com.glyphrz.glyphrize.model.Activity;
import com.glyphrz.glyphrize.model.Font;
import com.glyphrz.glyphrize.model.Glyph;
import com.glyphrz.glyphrize.model.User;
import com.glyphrz.glyphrize.repository.*;
import com.glyphrz.glyphrize.utils.Unicodes;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
public class MainController {
    private final GlyphRepository glyphRepository;
    private final FontRepository fontRepository;
    private final UserRepository userRepository;
    private final ActivityRepository activityRepository;
    private final ConfigRepository configRepository;

    //构造器注入
    @Autowired
    MainController(GlyphRepository glyphRepository, FontRepository fontRepository, UserRepository userRepository, ActivityRepository activityRepository, ConfigRepository configRepository) {
        this.glyphRepository = glyphRepository;
        this.fontRepository = fontRepository;
        this.userRepository = userRepository;
        this.activityRepository = activityRepository;
        this.configRepository = configRepository;
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
            fontCloud.setIsRecommend(0);
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
                        glyphLocal.setId(glyphCloud.getId());
                        Glyph result = glyphRepository.save(glyphLocal);
                        System.out.printf("字形%s已被替换为%s\n", glyphCloud, result);
                        //glyphsCloudNewer.add(glyphLocal);
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
                fontLocal.setIsRecommend(0);
                fontLocal.setCount(count);
                fontLocal.setGbkCount(gbkCount);
                fontRepository.save(fontLocal);
                fontSyncResponse.setFont(fontLocal);
            } else {
                fontCloud.setIsRecommend(0);
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
}

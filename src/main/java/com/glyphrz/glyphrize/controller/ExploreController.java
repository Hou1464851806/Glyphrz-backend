package com.glyphrz.glyphrize.controller;

import com.glyphrz.glyphrize.annotation.PassToken;
import com.glyphrz.glyphrize.entity.explore.ExploreFont;
import com.glyphrz.glyphrize.entity.explore.ExploreFontEntity;
import com.glyphrz.glyphrize.entity.explore.ExploreFontRequest;
import com.glyphrz.glyphrize.entity.explore.ExploreFontsEntity;
import com.glyphrz.glyphrize.model.Font;
import com.glyphrz.glyphrize.model.Glyph;
import com.glyphrz.glyphrize.model.User;
import com.glyphrz.glyphrize.repository.*;
import com.glyphrz.glyphrize.utils.Unicodes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Optional;

@RestController
public class ExploreController {
    private final GlyphRepository glyphRepository;
    private final FontRepository fontRepository;
    private final UserRepository userRepository;
    private final ActivityRepository activityRepository;
    private final ConfigRepository configRepository;

    //构造器注入
    @Autowired
    ExploreController(GlyphRepository glyphRepository, FontRepository fontRepository, UserRepository userRepository, ActivityRepository activityRepository, ConfigRepository configRepository) {
        this.glyphRepository = glyphRepository;
        this.fontRepository = fontRepository;
        this.userRepository = userRepository;
        this.activityRepository = activityRepository;
        this.configRepository = configRepository;
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
}

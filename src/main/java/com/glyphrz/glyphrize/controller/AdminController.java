package com.glyphrz.glyphrize.controller;

import com.glyphrz.glyphrize.annotation.PassToken;
import com.glyphrz.glyphrize.entity.font.FontsResponse;
import com.glyphrz.glyphrize.entity.user.UsersResponse;
import com.glyphrz.glyphrize.model.Font;
import com.glyphrz.glyphrize.model.FontBin;
import com.glyphrz.glyphrize.model.Glyph;
import com.glyphrz.glyphrize.model.User;
import com.glyphrz.glyphrize.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Optional;

@RestController
public class AdminController {
    private final GlyphRepository glyphRepository;
    private final FontRepository fontRepository;
    private final UserRepository userRepository;
    private final ActivityRepository activityRepository;
    private final ConfigRepository configRepository;

    private final FontBinRepository fontBinRepository;

    //构造器注入
    @Autowired
    AdminController(GlyphRepository glyphRepository, FontRepository fontRepository, UserRepository userRepository, ActivityRepository activityRepository, ConfigRepository configRepository, FontBinRepository fontBinRepository) {
        this.glyphRepository = glyphRepository;
        this.fontRepository = fontRepository;
        this.userRepository = userRepository;
        this.activityRepository = activityRepository;
        this.configRepository = configRepository;
        this.fontBinRepository = fontBinRepository;
    }

    @PassToken
    @GetMapping("/admin/font/recommend")
    public ResponseEntity<String> recommendFont(@RequestParam long fontKey, @RequestParam int isRecommend) {
        System.out.println(fontKey);
        System.out.println(isRecommend);
        Font font = fontRepository.findFontByFontKey(fontKey);
        font.setIsRecommend(isRecommend);
        Font result = fontRepository.save(font);
        System.out.println(result);
        return ResponseEntity.ok().build();
    }

    @PassToken
    @GetMapping("/admin/user/get")
    public ResponseEntity<UsersResponse> getAllUsers() {
        UsersResponse usersResponse = new UsersResponse();
        ArrayList<User> users = (ArrayList<User>) userRepository.findAll();
        usersResponse.setUsers(users);
        return ResponseEntity.ok().body(usersResponse);
    }

    @PassToken
    @GetMapping("/admin/font/get")
    public ResponseEntity<FontsResponse> getAllFonts() {
        FontsResponse fontsResponse = new FontsResponse();
        ArrayList<Font> fonts = (ArrayList<Font>) fontRepository.findAll();
        fontsResponse.setFonts(fonts);
        return ResponseEntity.ok().body(fontsResponse);
    }

    @PassToken
    @GetMapping("/admin/glyph/get")
    public ResponseEntity<ArrayList<Glyph>> getAllGlyphs(@RequestParam long fontKey) {
        ArrayList<Glyph> glyphs = glyphRepository.findAllByFontKey(fontKey);
        return ResponseEntity.ok().body(glyphs);
    }

    @PassToken
    @GetMapping("/admin/font/delete")
    public ResponseEntity<String> deleteFont(@RequestParam long fontKey) {
        Font font = fontRepository.findFontByFontKey(fontKey);
        FontBin tmp = new FontBin();
        tmp.setFontBin(font);
        FontBin fontBin = fontBinRepository.save(tmp);
        fontRepository.delete(font);
        font = fontRepository.findFontByUserId(fontKey);
        if (font == null) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(500).build();
        }
    }

    @PassToken
    @GetMapping("/admin/user/delete")
    public ResponseEntity<String> deleteUser(@RequestParam long id) {
        System.out.println(id);
        User u;
        Optional<User> user = userRepository.findById(Integer.parseInt(String.valueOf(id)));
        if (user.isEmpty()) {
            return ResponseEntity.status(500).build();
        } else {
            u = user.get();
        }
        ArrayList<Font> fontsForUser = fontRepository.findFontsByUserId(u.getId());
        for (Font font : fontsForUser) {
            FontBin tmp = new FontBin();
            tmp.setFontBin(font);
            FontBin fontBin = fontBinRepository.save(tmp);
            fontRepository.delete(font);
        }
        u.setPassword("114514");
        User result = userRepository.save(u);
        System.out.println(result);
        return ResponseEntity.ok().build();
    }
}

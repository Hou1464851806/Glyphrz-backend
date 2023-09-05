package com.glyphrz.glyphrize;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.FilterOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

import static com.glyphrz.glyphrize.GlobalVariable.sessionUserMap;

@RestController
public class MainController {
    private final GlyphRepository glyphRepository;
    private final FontRepository fontRepository;
    private UserRepository userRepository;

    @Autowired
    MainController(GlyphRepository glyphRepository, FontRepository fontRepository, UserRepository userRepository) {
        this.glyphRepository = glyphRepository;
        this.fontRepository = fontRepository;
        this.userRepository = userRepository;
    }

//    @PostMapping("/font")
//    public @ResponseBody NewFontResponse AddNewFont(@RequestBody Font newFont) {
//        long timestamp = System.currentTimeMillis();
//        newFont.setCreatedTime(timestamp);
//        newFont.setUpdateTime(timestamp);
//        newFont.setGbkCount(0);
//        Font result = fontRepository.save(newFont);
//        System.out.println(result.getFontKey());
//        Response response = new Response(200, "success");
//        return new NewFontResponse(response, result.getFontKey(), result.getCreatedTime());
//    }

//    @PutMapping("/font/{fontKey}")
//    public @ResponseBody Response UpdateFont(@PathVariable long fontKey, @RequestBody UpdateFontRequest updatedFontReq) {
//        Font updatedFont = updatedFontReq.getFont();
//        Glyph[] glyphs = updatedFontReq.getGlyphs();
//        long timestamp = System.currentTimeMillis();
//        Optional<Font> oldFont = fontRepository.findById(Integer.parseInt(String.valueOf(fontKey)));
//        oldFont.ifPresent(font -> updatedFont.setTime(font.getCreatedTime(), timestamp));
//        updatedFont.setFontKey(fontKey);
//        for (Glyph glyph : glyphs) {
//            Glyph oldGlyph = glyphRepository.findByUnicode(glyph.getUnicode());
//            if (oldGlyph == null) {
//                glyphRepository.save(glyph);
//            } else {
//                glyph.setId(oldGlyph.getId());
//                glyphRepository.save(glyph);
//            }
//        }
//        Font fontResult = fontRepository.save(updatedFont);
//        System.out.println(fontResult.getFontKey());
//        return new Response(200, "success");
//    }

    //    @GetMapping("/font/{fontKey}/glyphs")
//    public @ResponseBody GetGlyphResponse getGlyphs(@PathVariable long fontKey, @RequestParam String unicodes) {
//        long counter = 0;
//        String[] unicodesList = unicodes.split(",");
//        if (unicodesList.length == 0) {
//            return new GetGlyphResponse(new Response(400, "require unicodes"), new ArrayList<>());
//        }
//        ArrayList<Glyph> glyphs = new ArrayList<>();
//        System.out.println(glyphs);
//        for (String unicode : unicodesList) {
//            Glyph glyph = glyphRepository.findByUnicodeAndFontKey(unicode, fontKey);
//            if (glyph != null) {
//                glyphs.add(glyph);
//                counter = counter + 1;
//            }
//        }
//        System.out.println(counter);
//        if (counter != unicodesList.length) {
//            return new GetGlyphResponse(new Response(200, "some glyphs not exist"), glyphs);
//        }
//        return new GetGlyphResponse(new Response(200, "success"), glyphs);
//    }
    @PostMapping("/user/login")
    public ResponseEntity<UserLoginResponse> UserLogin(@RequestBody User loginInfo, HttpServletResponse response) {
        String uuid = UUID.randomUUID().toString();
        UserLoginResponse userLoginResponse = new UserLoginResponse();
        User user = userRepository.findByName(loginInfo.getName());
        if (user == null) {
            User registerInfo = userRepository.save(loginInfo);
            sessionUserMap.put(uuid, Integer.parseInt(String.valueOf(registerInfo.getId())));
            userLoginResponse.setFonts(new ArrayList<>());
        } else if (!user.getPassword().equals(loginInfo.getPassword())) {
            return ResponseEntity.status(403).build();
        } else {
            sessionUserMap.put(uuid, Integer.parseInt(String.valueOf(user.getId())));
            Iterable<Font> fonts = fontRepository.findFontsByUserId(user.getId());
            userLoginResponse.setAvatar(user.getAvatar());
            userLoginResponse.setSignature(user.getSignature());
            userLoginResponse.setFonts(fonts);
        }
        System.out.println(sessionUserMap);
        //userLoginResponse.setSessionId(uuid);
        Cookie sessionId = new Cookie("sessionId", uuid);
        sessionId.setSecure(false);
        sessionId.setHttpOnly(true);
        response.addCookie(sessionId);
        return ResponseEntity.ok().body(userLoginResponse);
    }

    @GetMapping("/user/logout")
    public ResponseEntity<String> UserLogout(HttpServletRequest request,HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        String sessionId = cookies[0].getValue();
        System.out.println(sessionId);
        Integer userId = sessionUserMap.get(sessionId);
        System.out.println(userId);
        if (userId == null) {
            return ResponseEntity.status(403).build();
        } else {
            sessionUserMap.remove(sessionId);
            System.out.println(sessionUserMap);
            cookies[0].setMaxAge(0);
            response.addCookie(cookies[0]);
            return ResponseEntity.ok().build();
        }
    }
//    @PostMapping("font/sync")
//    public ResponseEntity<FontSyncResponse> FontSync(@CookieValue String sessionId,@RequestBody FontSyncRequest fontSyncRequest){
//
//    }
}

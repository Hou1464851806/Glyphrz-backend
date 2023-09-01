package com.glyphrz.glyphrize;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Optional;

@RestController
public class MainController {
    @Autowired
    private GlyphRepository glyphRepository;
    @Autowired
    private FontRepository fontRepository;

    @PostMapping("/font")
    public @ResponseBody NewFontResponse AddNewFont(@RequestBody Font newFont) {
        long timestamp = System.currentTimeMillis();
        newFont.setCreatedTime(timestamp);
        newFont.setUpdateTime(timestamp);
        newFont.setGb2312Count(0);
        Font result = fontRepository.save(newFont);
        System.out.println(result.getFontKey());
        Response response = new Response(200, "success");
        return new NewFontResponse(response, result.getFontKey(), result.getCreatedTime());
    }

    @PutMapping("/font/{fontKey}")
    public @ResponseBody Response UpdateFont(@PathVariable long fontKey, @RequestBody UpdateFontRequest updatedFontReq) {
        Font updatedFont = updatedFontReq.getFont();
        Glyph[] glyphs = updatedFontReq.getGlyphs();
        long timestamp = System.currentTimeMillis();
        Optional<Font> oldFont = fontRepository.findById(Integer.parseInt(String.valueOf(fontKey)));
        oldFont.ifPresent(font -> updatedFont.setTime(font.getCreatedTime(), timestamp));
        updatedFont.setFontKey(fontKey);
        for (Glyph glyph : glyphs) {
            Glyph oldGlyph = glyphRepository.findByUnicode(glyph.getUnicode());
            if (oldGlyph == null) {
                glyphRepository.save(glyph);
            } else {
                glyph.setId(oldGlyph.getId());
                glyphRepository.save(glyph);
            }
        }
        Font fontResult = fontRepository.save(updatedFont);
        System.out.println(fontResult.getFontKey());
        return new Response(200, "success");
    }

    @GetMapping("/font/{fontKey}/glyphs")
    public @ResponseBody GetGlyphResponse getGlyphs(@PathVariable long fontKey, @RequestParam String unicodes) {
        long counter = 0;
        String[] unicodesList = unicodes.split(",");
        if (unicodesList.length == 0) {
            return new GetGlyphResponse(new Response(400, "require unicodes"), new ArrayList<>());
        }
        ArrayList<Glyph> glyphs = new ArrayList<>();
        System.out.println(glyphs);
        for (String unicode : unicodesList) {
            Glyph glyph = glyphRepository.findByUnicodeAndFontKey(unicode, fontKey);
            if (glyph != null) {
                glyphs.add(glyph);
                counter = counter + 1;
            }
        }
        System.out.println(counter);
        if (counter != unicodesList.length) {
            return new GetGlyphResponse(new Response(200, "some glyphs not exist"), glyphs);
        }
        return new GetGlyphResponse(new Response(200, "success"), glyphs);
    }
}

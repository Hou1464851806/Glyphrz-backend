package com.glyphrz.glyphrize.repository;

import com.glyphrz.glyphrize.model.Glyph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;

public interface GlyphRepository extends JpaRepository<Glyph, Integer> {
    Glyph findByUnicode(String unicode);

    Glyph findByUnicodeAndFontKey(String unicode, long fontKey);

    ArrayList<Glyph> findAllByTimeGreaterThanAndFontKey(long syncTime, long fontKey);

    ArrayList<Glyph> findAllByFontKey(long fontKey);

    long countByIsGbkAndFontKey(int isGbk, long fontKey);

    long countByFontKey(long fontKey);

}

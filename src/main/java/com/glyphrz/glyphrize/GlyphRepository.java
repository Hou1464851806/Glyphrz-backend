package com.glyphrz.glyphrize;

import jakarta.annotation.Nullable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface GlyphRepository extends JpaRepository<Glyph, Integer> {
    @Nullable
    Glyph findByUnicode(String unicode);
    Glyph findByUnicodeAndFontKey(String unicode, long fontKey);
}

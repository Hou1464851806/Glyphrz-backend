package com.glyphrz.glyphrize;

import jakarta.annotation.Nullable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GlyphRepository extends JpaRepository<Glyph, Integer> {
    @Nullable
    Glyph findByUnicode(String unicode);

    Glyph findByUnicodeAndFontKey(String unicode, long fontKey);
}

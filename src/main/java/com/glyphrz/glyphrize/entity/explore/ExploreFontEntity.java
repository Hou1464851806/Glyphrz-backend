package com.glyphrz.glyphrize.entity.explore;

import com.glyphrz.glyphrize.model.Glyph;

import java.util.ArrayList;

public class ExploreFontEntity {
    ArrayList<Glyph> glyphs;

    @Override
    public String toString() {
        return "ExploreFontEntity{" +
                "glyphs=" + glyphs +
                '}';
    }

    public ArrayList<Glyph> getGlyphs() {
        return glyphs;
    }

    public void setGlyphs(ArrayList<Glyph> glyphs) {
        this.glyphs = glyphs;
    }
}

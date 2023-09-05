package com.glyphrz.glyphrize;

import java.util.ArrayList;

public class FontSyncResponse {
   private long fontKey;
   private ArrayList<Glyph> glyphs;

    public long getFontKey() {
        return fontKey;
    }

    public void setFontKey(long fontKey) {
        this.fontKey = fontKey;
    }

    public ArrayList<Glyph> getGlyphs() {
        return glyphs;
    }

    public void setGlyphs(ArrayList<Glyph> glyphs) {
        this.glyphs = glyphs;
    }
}

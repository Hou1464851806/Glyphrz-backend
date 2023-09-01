package com.glyphrz.glyphrize;

public class UpdateFontRequest {
    private Font font;
    private Glyph[] glyphs;

    public Font getFont() {
        return font;
    }

    public void setFont(Font font) {
        this.font = font;
    }

    public Glyph[] getGlyphs() {
        return glyphs;
    }

    public void setGlyphs(Glyph[] glyphs) {
        this.glyphs = glyphs;
    }
}

package com.apriverse.glyphz.entity.font;

import com.apriverse.glyphz.model.Font;

import java.util.ArrayList;

public class FontsResponse {
    private ArrayList<Font> fonts;

    @Override
    public String toString() {
        return "FontsResponse{" + "Fonts=" + fonts + '}';
    }

    public ArrayList<Font> getFonts() {
        return fonts;
    }

    public void setFonts(ArrayList<Font> fonts) {
        this.fonts = fonts;
    }
}

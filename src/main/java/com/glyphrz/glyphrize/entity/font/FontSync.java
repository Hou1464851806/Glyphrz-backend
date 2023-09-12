package com.apriverse.glyphz.entity.font;

import com.apriverse.glyphz.model.Activity;
import com.apriverse.glyphz.model.Font;
import com.apriverse.glyphz.model.Glyph;

import java.util.ArrayList;

public class FontSync {
    private String userName;
    private Font font;
    private ArrayList<Glyph> glyphs;
    private ArrayList<Activity> activities;

    public FontSync() {
        this.font = null;
        this.glyphs = new ArrayList<>();
        this.activities = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "FontSync{" + "userName='" + userName + '\'' + ", font=" + font + ", glyphs=" + glyphs + ", activities=" + activities + '}';
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Font getFont() {
        return font;
    }

    public void setFont(Font font) {
        this.font = font;
    }

    public ArrayList<Glyph> getGlyphs() {
        return glyphs;
    }

    public void setGlyphs(ArrayList<Glyph> glyphs) {
        this.glyphs = glyphs;
    }

    public ArrayList<Activity> getActivities() {
        return activities;
    }

    public void setActivities(ArrayList<Activity> activities) {
        this.activities = activities;
    }
}

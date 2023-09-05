package com.glyphrz.glyphrize;

import jakarta.persistence.*;

@Entity
public class Glyph {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private long fontKey;
    private String unicode;
    private String svg;
    private long time;

    public String getUnicode() {
        return unicode;
    }

    public void setUnicode(String unicode) {
        this.unicode = unicode;
    }

    public String getSvg() {
        return svg;
    }

    public void setSvg(String svg) {
        this.svg = svg;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getFontKey() {
        return fontKey;
    }

    public void setFontKey(long fontKey) {
        this.fontKey = fontKey;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}

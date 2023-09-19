package com.apriverse.glyphz.entity.explore;

public class ExploreFontRequest {
    private long fontKey;
    private String preview;

    @Override
    public String toString() {
        return "ExploreFontRequest{" +
                "fontKey=" + fontKey +
                ", preview='" + preview + '\'' +
                '}';
    }

    public long getFontKey() {
        return fontKey;
    }

    public void setFontKey(long fontKey) {
        this.fontKey = fontKey;
    }

    public String getPreview() {
        return preview;
    }

    public void setPreview(String preview) {
        this.preview = preview;
    }
}

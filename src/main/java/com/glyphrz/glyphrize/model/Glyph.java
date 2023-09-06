package com.glyphrz.glyphrize.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
public class Glyph {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonIgnore
    private long id;
    @JsonIgnore
    private long fontKey;//和所属字体对应
    private String unicode;//字体unicode码
    @Column(columnDefinition = "varchar(8192)")
    private String svg;//字形描述
    private long time;//用来云同步
    //字形基本信息
    private long xMin;
    private long xMax;
    private long yMin;
    private long yMax;
    private long marginRight;
    private int isGbk;//用于统计生成gbkCount

    @Override
    public String toString() {
        return "Glyph{" + "id=" + id + ", fontKey=" + fontKey + ", unicode='" + unicode + '\'' + ", time=" + time + ", xMin=" + xMin + ", xMax=" + xMax + ", yMin=" + yMin + ", yMax=" + yMax + ", marginRight=" + marginRight + ", isGbk=" + isGbk + '}';
    }

    public int getIsGbk() {
        return isGbk;
    }

    public void setIsGbk(int isGbk) {
        this.isGbk = isGbk;
    }

    public long getxMin() {
        return xMin;
    }

    public void setxMin(long xMin) {
        this.xMin = xMin;
    }

    public long getxMax() {
        return xMax;
    }

    public void setxMax(long xMax) {
        this.xMax = xMax;
    }

    public long getyMin() {
        return yMin;
    }

    public void setyMin(long yMin) {
        this.yMin = yMin;
    }

    public long getyMax() {
        return yMax;
    }

    public void setyMax(long yMax) {
        this.yMax = yMax;
    }

    public long getMarginRight() {
        return marginRight;
    }

    public void setMarginRight(long marginRight) {
        this.marginRight = marginRight;
    }

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

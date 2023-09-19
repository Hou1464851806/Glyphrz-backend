package com.apriverse.glyphz.entity.explore;

import com.apriverse.glyphz.model.Font;
import com.apriverse.glyphz.model.Glyph;

import java.util.ArrayList;

public class ExploreFont {
    private long fontKey;
    private String name;
    private String enName;
    private String description;
    private String copyright;
    private String trademark;
    private String license;
    private long createTime;
    private long updateTime;
    private long infoUpdateTime;//用来同步字体信息


    private long syncTime;//用来同步字形信息和热力信息
    private long gbkCount;//通过数据库检索得出
    private long count;//通过数据库检索得出

    private long userId;//通过token解析并对应存储
    private int isRecommend;//是否推荐到广场

    private String userName;

    private ArrayList<Glyph> glyphs;

    public void setExploreFont(Font fontModel) {
        this.fontKey = fontModel.getFontKey();
        this.name = fontModel.getName();
        this.enName = fontModel.getEnName();
        this.copyright = fontModel.getCopyright();
        this.description = fontModel.getDescription();
        this.license = fontModel.getLicense();
        this.trademark = fontModel.getTrademark();
        this.createTime = fontModel.getCreateTime();
        this.infoUpdateTime = fontModel.getInfoUpdateTime();
        this.updateTime = fontModel.getUpdateTime();
        this.syncTime = fontModel.getSyncTime();
        this.gbkCount = fontModel.getGbkCount();
        this.count = fontModel.getCount();
        this.userId = fontModel.getUserId();
        this.isRecommend = fontModel.getIsRecommend();
    }

    @Override
    public String toString() {
        return "ExploreFont{" + "fontKey=" + fontKey + ", name='" + name + '\'' + ", enName='" + enName + '\'' + ", description='" + description + '\'' + ", copyright='" + copyright + '\'' + ", trademark='" + trademark + '\'' + ", license='" + license + '\'' + ", createTime=" + createTime + ", updateTime=" + updateTime + ", infoUpdateTime=" + infoUpdateTime + ", syncTime=" + syncTime + ", gbkCount=" + gbkCount + ", count=" + count + ", userId=" + userId + ", isRecommend=" + isRecommend + ", userName='" + userName + '\'' + ", glyphs=" + glyphs + '}';
    }

    public long getFontKey() {
        return fontKey;
    }

    public void setFontKey(long fontKey) {
        this.fontKey = fontKey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEnName() {
        return enName;
    }

    public void setEnName(String enName) {
        this.enName = enName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCopyright() {
        return copyright;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    public String getTrademark() {
        return trademark;
    }

    public void setTrademark(String trademark) {
        this.trademark = trademark;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public long getInfoUpdateTime() {
        return infoUpdateTime;
    }

    public void setInfoUpdateTime(long infoUpdateTime) {
        this.infoUpdateTime = infoUpdateTime;
    }

    public long getSyncTime() {
        return syncTime;
    }

    public void setSyncTime(long syncTime) {
        this.syncTime = syncTime;
    }

    public long getGbkCount() {
        return gbkCount;
    }

    public void setGbkCount(long gbkCount) {
        this.gbkCount = gbkCount;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public int getIsRecommend() {
        return isRecommend;
    }

    public void setIsRecommend(int isRecommend) {
        this.isRecommend = isRecommend;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public ArrayList<Glyph> getGlyphs() {
        return glyphs;
    }

    public void setGlyphs(ArrayList<Glyph> glyphs) {
        this.glyphs = glyphs;
    }
}

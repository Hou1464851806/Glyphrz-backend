package com.glyphrz.glyphrize;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Font {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long fontKey;
    private String name;
    private String description;
    private long createdTime;
    private long updateTime;
    private long gb2312Count;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(long createdTime) {
        this.createdTime = createdTime;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public long getGb2312Count() {
        return gb2312Count;
    }

    public void setGb2312Count(long gb2312Count) {
        this.gb2312Count = gb2312Count;
    }

    public void updateFont(long fontKey, String name, String description, long createdTime, long updateTime, long gb2312Count) {
        this.fontKey = fontKey;
        this.name = name;
        this.description = description;
        this.createdTime = createdTime;
        this.updateTime = updateTime;
        this.gb2312Count = gb2312Count;
    }

    public void setTime(long createdTime, long updateTime) {
        this.setCreatedTime(createdTime);
        this.setUpdateTime(updateTime);
    }
}

package com.apriverse.glyphz.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Activity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonIgnore
    private long id;
    @JsonIgnore
    private long fontKey;//和所属字体对应
    private long day;//用来云同步
    private long activity;//热力值

    @Override
    public String toString() {
        return "Activity{" + "id=" + id + ", fontKey=" + fontKey + ", day=" + day + ", activity=" + activity + '}';
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

    public long getDay() {
        return day;
    }

    public void setDay(long day) {
        this.day = day;
    }

    public long getActivity() {
        return activity;
    }

    public void setActivity(long activity) {
        this.activity = activity;
    }
}

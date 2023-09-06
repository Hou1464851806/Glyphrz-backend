package com.glyphrz.glyphrize.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Config {
    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    @JsonIgnore
//    private long id;

    @JsonIgnore
    private long userId;

    private int background;

    private int allowTouch;
    private int allowMouse;
    private int allowPencil;

    private long gridType;
    private long lineWidthRatio;
    private double lineWidthChangeRatio;
    private double minForce;
    private double maxForce;
    private int brushType;

    @Override
    public String toString() {
        return "Config{" + "userId=" + userId + ", background=" + background + ", allowTouch=" + allowTouch + ", allowMouse=" + allowMouse + ", allowPencil=" + allowPencil + ", gridType=" + gridType + ", lineWidthRatio=" + lineWidthRatio + ", lineWidthChangeRatio=" + lineWidthChangeRatio + ", minForce=" + minForce + ", maxForce=" + maxForce + ", brushType=" + brushType + '}';
    }
//    public long getId() {
//        return id;
//    }
//
//    public void setId(long id) {
//        this.id = id;
//    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long user_id) {
        this.userId = user_id;
    }

    public int getBackground() {
        return background;
    }

    public void setBackground(int background) {
        this.background = background;
    }

    public int getAllowTouch() {
        return allowTouch;
    }

    public void setAllowTouch(int allowTouch) {
        this.allowTouch = allowTouch;
    }

    public int getAllowMouse() {
        return allowMouse;
    }

    public void setAllowMouse(int allowMouse) {
        this.allowMouse = allowMouse;
    }

    public int getAllowPencil() {
        return allowPencil;
    }

    public void setAllowPencil(int allowPencil) {
        this.allowPencil = allowPencil;
    }

    public long getGridType() {
        return gridType;
    }

    public void setGridType(long gridType) {
        this.gridType = gridType;
    }

    public long getLineWidthRatio() {
        return lineWidthRatio;
    }

    public void setLineWidthRatio(long lineWidthRatio) {
        this.lineWidthRatio = lineWidthRatio;
    }

    public double getLineWidthChangeRatio() {
        return lineWidthChangeRatio;
    }

    public void setLineWidthChangeRatio(double lineWidthChangeRatio) {
        this.lineWidthChangeRatio = lineWidthChangeRatio;
    }

    public double getMinForce() {
        return minForce;
    }

    public void setMinForce(double minForce) {
        this.minForce = minForce;
    }

    public double getMaxForce() {
        return maxForce;
    }

    public void setMaxForce(double maxForce) {
        this.maxForce = maxForce;
    }

    public int getBrushType() {
        return brushType;
    }

    public void setBrushType(int brushType) {
        this.brushType = brushType;
    }
}

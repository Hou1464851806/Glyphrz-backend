package com.glyphrz.glyphrize.entity.explore;

import com.glyphrz.glyphrize.model.Font;

import java.util.ArrayList;

public class ExploreFontsEntity {
    private ArrayList<ExploreFont> exploreFonts;

    @Override
    public String toString() {
        return "ExploreFontsEntity{" +
                "exploreFonts=" + exploreFonts +
                '}';
    }

    public ArrayList<ExploreFont> getExploreFonts() {
        return exploreFonts;
    }

    public void setExploreFonts(ArrayList<ExploreFont> exploreFonts) {
        this.exploreFonts = exploreFonts;
    }
}

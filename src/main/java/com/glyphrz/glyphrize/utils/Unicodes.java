package com.glyphrz.glyphrize.utils;

import java.util.ArrayList;

public class Unicodes {
    public static ArrayList<String> String2Unicodes(String origin) {
        ArrayList<String> unicodes = new ArrayList<>();
        for (char c : origin.toCharArray()) {
            unicodes.add(Integer.toHexString(c));
        }
        return unicodes;
    }
}

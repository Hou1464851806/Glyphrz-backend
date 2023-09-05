package com.glyphrz.glyphrize;

import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class GlobalVariable {
    public static final HashMap<String ,Integer> sessionUserMap = new HashMap<>();
}

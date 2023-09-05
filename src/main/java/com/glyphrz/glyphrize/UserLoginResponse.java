package com.glyphrz.glyphrize;

public class UserLoginResponse {
    //private String sessionId;
    private String signature;
    private String avatar;
    private Iterable<Font> fonts;

//    public String getSessionId() {
//        return sessionId;
//    }
//
//    public void setSessionId(String sessionId) {
//        this.sessionId = sessionId;
//    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Iterable<Font> getFonts() {
        return fonts;
    }

    public void setFonts(Iterable<Font> fonts) {
        this.fonts = fonts;
    }
}

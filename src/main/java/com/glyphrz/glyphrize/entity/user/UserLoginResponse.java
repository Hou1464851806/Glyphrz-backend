package com.glyphrz.glyphrize.entity.user;

import com.glyphrz.glyphrize.model.Config;

public class UserLoginResponse {
    //private String sessionId;
    private String signature;
    private String avatar;
    private Config config;//用户设置信息

    public UserLoginResponse() {
        this.signature = "";
        this.avatar = "";
    }

    public Config getConfig() {
        return config;
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    @Override
    public String toString() {
        return "UserLoginResponse{" + "signature='" + signature + '\'' + ", avatar='" + avatar + '\'' + ", config=" + config + '}';
    }

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


}

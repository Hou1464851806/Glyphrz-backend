package com.apriverse.glyphz.model;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import jakarta.persistence.*;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(unique = true)
    private String name;//用于检验用户是否存在
    private String password;//验证签名和登录验证
    private String signature;//用户简介

    private String avatar;//用户头像url

    @Override
    public String toString() {
        return "User{" + "id=" + id + ", name='" + name + '\'' + ", signature='" + signature + '\'' + ", avatar='" + avatar + '\'' + '}';
    }

    //通过userName和password生成token
    public String getToken() {
        return JWT.create().withAudience(String.valueOf(this.name)).sign(Algorithm.HMAC256(this.password));
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

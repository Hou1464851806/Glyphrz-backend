package com.glyphrz.glyphrize.entity;


import com.glyphrz.glyphrize.model.User;

import java.util.ArrayList;

public class UsersResponse {
    private ArrayList<User> users;

    @Override
    public String toString() {
        return "UsersResponse{" + "users=" + users + '}';
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }
}

package com.glyphrz.glyphrize;

public class NewFontResponse {
    private long fontKey;
    private long createdTime;
    private Response response;

    public NewFontResponse(Response response, long fontKey, long createdTime) {
        this.fontKey = fontKey;
        this.createdTime = createdTime;
        this.response = response;
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(long createdTime) {
        this.createdTime = createdTime;
    }

    public long getFontKey() {
        return fontKey;
    }

    public void setFontKey(long fontKey) {
        this.fontKey = fontKey;
    }
}

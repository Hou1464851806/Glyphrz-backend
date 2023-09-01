package com.glyphrz.glyphrize;

import java.util.ArrayList;

public class GetGlyphResponse {
    private Response response;
    private ArrayList<Glyph> glyphs;

    public GetGlyphResponse(Response response, ArrayList<Glyph> glyphs) {
        this.response = response;
        this.glyphs = glyphs;
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public ArrayList<Glyph> getGlyphs() {
        return glyphs;
    }

    public void setGlyphs(ArrayList<Glyph> glyphs) {
        this.glyphs = glyphs;
    }
}

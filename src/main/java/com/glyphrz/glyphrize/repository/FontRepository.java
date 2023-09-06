package com.glyphrz.glyphrize.repository;

import com.glyphrz.glyphrize.model.Font;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;

public interface FontRepository extends JpaRepository<Font, Integer> {
    ArrayList<Font> findFontsByUserId(long id);

    Font findFontByFontKey(long fontKey);

    Font findFontByUserId(long id);

    ArrayList<Font> findFontsByIsPublic(int isPublic);

    ArrayList<Font> findFontsByIsRecommend(int isRecommend);
}

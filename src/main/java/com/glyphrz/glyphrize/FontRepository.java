package com.glyphrz.glyphrize;

import jakarta.annotation.Nullable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.ArrayList;

public interface FontRepository extends JpaRepository<Font, Integer> {
    Iterable<Font> findFontsByUserId(long id);
    Iterable<Font> findFontByFontKey(long fontKey);
}

package com.glyphrz.glyphrize.repository;

import com.glyphrz.glyphrize.model.Config;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfigRepository extends JpaRepository<Config, Integer> {
    Config findByUserId(long userId);
}

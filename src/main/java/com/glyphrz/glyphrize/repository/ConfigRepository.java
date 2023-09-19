package com.apriverse.glyphz.repository;

import com.apriverse.glyphz.model.Config;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfigRepository extends JpaRepository<Config, Integer> {
    Config findByUserId(long userId);
}

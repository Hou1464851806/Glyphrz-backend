package com.apriverse.glyphz.repository;

import com.apriverse.glyphz.model.Activity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;

public interface ActivityRepository extends JpaRepository<Activity, Integer> {
    Activity findByDayAndFontKey(long day, long fontKey);

    ArrayList<Activity> findAllByDayGreaterThanEqualAndFontKey(long syncTime, long fontKey);
}

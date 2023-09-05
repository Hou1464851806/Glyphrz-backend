package com.glyphrz.glyphrize;

import jakarta.annotation.Nullable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Integer> {
    @Nullable
    User findByName(String name);

}

package com.apriverse.glyphz.repository;

import com.apriverse.glyphz.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByName(String name);

    User findByNameAndPassword(String name, String password);
}

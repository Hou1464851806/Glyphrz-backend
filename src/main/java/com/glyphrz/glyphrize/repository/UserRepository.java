package com.glyphrz.glyphrize.repository;

import com.glyphrz.glyphrize.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByName(String name);

    User findByNameAndPassword(String name, String password);
}

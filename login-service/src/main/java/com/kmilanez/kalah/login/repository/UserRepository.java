package com.kmilanez.kalah.login.repository;

import com.kmilanez.kalah.login.domain.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
    User findByUsername(final String username);
}

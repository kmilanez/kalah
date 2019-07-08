package com.kmilanez.kalah.login.service;

import com.kmilanez.kalah.login.domain.entity.User;

public interface UserService {
    User add(final User user);
    User loadUserByUsername(final String username);
}

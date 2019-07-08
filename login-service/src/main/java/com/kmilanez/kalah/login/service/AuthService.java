package com.kmilanez.kalah.login.service;

import com.kmilanez.kalah.login.domain.entity.User;

public interface AuthService {
    String auth(final User user);
}

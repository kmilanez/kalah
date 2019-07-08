package com.kmilanez.kalah.login.service.impl;

import com.google.common.base.Preconditions;
import com.kmilanez.kalah.login.domain.exception.UserNotFoundException;
import com.kmilanez.kalah.login.domain.entity.User;
import com.kmilanez.kalah.login.repository.UserRepository;
import com.kmilanez.kalah.login.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository repository;
    private PasswordEncoder passwordEncoder;

    public UserServiceImpl(final UserRepository repository,
                           final PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User add(final User user) {
        checkRequiredFields(user);
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        User newUser = new User(user.getUsername(), encodedPassword);
        repository.save(newUser);
        return newUser;
    }

    private void checkRequiredFields(final User user) {
        Preconditions.checkArgument(!StringUtils.isEmpty(user.getUsername()),
                "Authentication error: Username is missing");
        Preconditions.checkArgument(!StringUtils.isEmpty(user.getPassword()),
                "Authentication error: Reason: Password is missing");
    }

    @Override
    public User loadUserByUsername(final String username) {
        User dbUser = repository.findByUsername(username);
        if (ObjectUtils.isEmpty(dbUser)) {
            throw new UserNotFoundException();
        }
        return dbUser;
    }
}

package com.kmilanez.kalah.login.service.impl;

import com.google.common.base.Preconditions;
import com.kmilanez.kalah.login.domain.exception.InvalidPasswordException;
import com.kmilanez.kalah.login.domain.exception.UserNotFoundException;
import com.kmilanez.kalah.login.domain.entity.User;
import com.kmilanez.kalah.login.service.AuthService;
import com.kmilanez.kalah.login.service.TokenService;
import com.kmilanez.kalah.login.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

@Service
public class AuthServiceImpl implements AuthService {

    private TokenService tokenService;
    private UserService userService;
    private PasswordEncoder passwordEncoder;

    public AuthServiceImpl(final TokenService tokenService,
                           final UserService userService,
                           final PasswordEncoder passwordEncoder) {
        this.tokenService = tokenService;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public String auth(final User user) {
        checkRequiredFields(user);
        User dbUser = userService.loadUserByUsername(user.getUsername());
        checkIfUserExists(dbUser);
        checkPasswordMatches(user.getPassword(), dbUser.getPassword());
        return tokenService.createToken(user.getUsername());
    }

    private void checkRequiredFields(final User user) {
        Preconditions.checkArgument(!StringUtils.isEmpty(user.getUsername()),
                "You Shall Not Pass! Reason: Username is missing");
        Preconditions.checkArgument(!StringUtils.isEmpty(user.getPassword()),
                "You Shall Not Pass! Reason: Password is missing");
    }

    private void checkIfUserExists(final User user) {
        if (ObjectUtils.isEmpty(user)) {
            throw new UserNotFoundException();
        }
    }

    private void checkPasswordMatches(final String inputPassword, final String dbPassword) {
        boolean invalidPassword = !passwordEncoder.matches(inputPassword, dbPassword);
        if (invalidPassword) {
            throw new InvalidPasswordException();
        }
    }
}

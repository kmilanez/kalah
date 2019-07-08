package com.kmilanez.kalah.service.impl;

import com.kmilanez.kalah.domain.exception.InvalidUserException;
import com.kmilanez.kalah.domain.integration.service.login.IntegrationUser;
import com.kmilanez.kalah.domain.integration.service.login.LoginServiceClient;
import com.kmilanez.kalah.domain.integration.service.login.ResponseForLoginService;
import com.kmilanez.kalah.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

@Service
public class UserServiceImpl implements UserService {

    private LoginServiceClient loginService;

    public UserServiceImpl(LoginServiceClient loginService) {
        this.loginService = loginService;
    }

    public String getUsername(String token) {
        ResponseForLoginService response = loginService.getUserFromToken(token);
        checkIfResponseIsEmpty(response);
        checkIfUserHasUsername(response.getUser());
        return response.getUser().getUsername();
    }

    private void checkIfResponseIsEmpty(ResponseForLoginService response) {
        if (ObjectUtils.isEmpty(response) || ObjectUtils.isEmpty(response.getUser())) {
            throw new InvalidUserException();
        }
    }

    private void checkIfUserHasUsername(IntegrationUser user) {
        if (StringUtils.isEmpty(user.getUsername())) {
            throw new InvalidUserException();
        }
    }
}

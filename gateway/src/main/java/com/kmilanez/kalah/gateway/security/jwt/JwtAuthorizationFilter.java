package com.kmilanez.kalah.gateway.security.jwt;

import com.kmilanez.kalah.gateway.integration.service.client.LoginServiceClient;
import com.kmilanez.kalah.gateway.integration.service.model.IntegrationUser;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

import static com.kmilanez.kalah.gateway.security.jwt.TokenConstants.JWT_HEADER_NAME;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private LoginServiceClient loginServiceClient;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager,
                                  LoginServiceClient loginServiceClient) {
        super(authenticationManager);
        this.loginServiceClient = loginServiceClient;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res,
                                    FilterChain chain) throws IOException, ServletException {

        String header = req.getHeader(JWT_HEADER_NAME);
        if (StringUtils.isEmpty(header)) {
            chain.doFilter(req, res);
        } else {
            UsernamePasswordAuthenticationToken authentication = getAuthentication(req);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            chain.doFilter(req, res);
        }
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(JWT_HEADER_NAME);
        IntegrationUser user = loginServiceClient.getUserFromToken(token).getUser();
        return new UsernamePasswordAuthenticationToken(
                user.getUsername(),
                null,
                Collections.emptyList()
        );
    }
}

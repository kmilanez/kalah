package com.kmilanez.kalah.login.controller;

import com.kmilanez.kalah.login.domain.entity.User;
import com.kmilanez.kalah.login.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private UserService userService;

    public UserController(final UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public User addUser(@RequestBody final User user) {
        return userService.add(user);
    }

    @GetMapping("/{username}")
    public User getUserByUsername(@PathVariable("username") final String username) {
        return userService.loadUserByUsername(username);
    }

}

package com.zhang.controller;

import com.zhang.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author zhang
 * @date 2024/7/31
 * @Description
 */

@Component
@RequestMapping
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    private void  login(@RequestParam("username") String username,
                        @RequestParam("password") String password) {
        userService.login(username, password);
    }

}

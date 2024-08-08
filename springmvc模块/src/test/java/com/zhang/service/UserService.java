package com.zhang.service;

import org.springframework.stereotype.Service;

/**
 * @author zhang
 * @date 2024/7/31
 * @Description
 */

@Service
public interface UserService {


    public void login(String username, String password);
}

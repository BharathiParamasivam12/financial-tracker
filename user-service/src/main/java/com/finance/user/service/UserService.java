package com.finance.user.service;

import com.finance.user.model.User;
import com.finance.user.model.UserInfo;

public interface UserService {
    void registerUser(User registrationDTO);
    void loginUser(User registrationDTO);
    User getUser(String username);
    void createUserInfo(UserInfo userInfo);
}
package com.crafts.platform.service;

import com.crafts.platform.dto.LoginRequest;
import com.crafts.platform.dto.RegisterRequest;
import com.crafts.platform.entity.User;

public interface UserService {

    void register(RegisterRequest request);

    User login(LoginRequest request);
}

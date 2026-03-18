package com.crafts.platform.service.impl;

import com.crafts.platform.dto.LoginRequest;
import com.crafts.platform.dto.RegisterRequest;
import com.crafts.platform.entity.User;
import com.crafts.platform.exception.BizException;
import com.crafts.platform.mapper.UserMapper;
import com.crafts.platform.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void register(RegisterRequest request) {
        User existing = userMapper.findByUsername(request.getUsername());
        if (existing != null) {
            throw new BizException("用户名已存在");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setPhone(request.getPhone());
        user.setEmail(request.getEmail());
        user.setRole(normalizeRole(request.getRole()));
        user.setStatus(1);

        int rows = userMapper.insert(user);
        if (rows != 1) {
            throw new BizException("注册失败，请稍后重试");
        }
    }

    @Override
    public User login(LoginRequest request) {
        User user = userMapper.findByUsername(request.getUsername());
        if (user == null) {
            throw new BizException("用户名或密码错误");
        }
        if (user.getStatus() != null && user.getStatus() == 0) {
            throw new BizException("账号已被禁用");
        }
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BizException("用户名或密码错误");
        }
        return user;
    }

    private String normalizeRole(String role) {
        if (!StringUtils.hasText(role)) {
            return "consumer";
        }
        if ("merchant".equals(role) || "admin".equals(role) || "consumer".equals(role)) {
            return role;
        }
        return "consumer";
    }
}

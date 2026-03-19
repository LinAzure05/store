package com.crafts.platform.service.impl;

import com.crafts.platform.dto.LoginRequest;
import com.crafts.platform.dto.RegisterRequest;
import com.crafts.platform.entity.User;
import com.crafts.platform.exception.BizException;
import com.crafts.platform.mapper.UserMapper;
import com.crafts.platform.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Locale;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    @Override
    public void register(RegisterRequest request) {
        String username = request.getUsername() == null ? null : request.getUsername().trim();
        if (!StringUtils.hasText(username)) {
            throw new BizException("用户名不能为空");
        }

        User existing = userMapper.findByUsername(username);
        if (existing != null) {
            throw new BizException("用户名已存在");
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(request.getPassword());
        user.setPhone(request.getPhone());
        user.setRole(normalizeRegisterRole(request.getRole()));
        user.setStatus(1);

        int rows = userMapper.insert(user);
        if (rows != 1) {
            throw new BizException("注册失败，请稍后重试");
        }
    }

    @Override
    public User login(LoginRequest request) {
        String username = request.getUsername() == null ? null : request.getUsername().trim();
        User user = userMapper.findByUsername(username);
        if (user == null) {
            throw new BizException("用户名或密码错误");
        }
        if (user.getStatus() != null && user.getStatus() == 0) {
            throw new BizException("账号已被禁用");
        }
        if (!request.getPassword().equals(user.getPassword())) {
            throw new BizException("用户名或密码错误");
        }
        user.setRole(normalizeSystemRole(user.getRole()));
        return user;
    }

    private String normalizeRegisterRole(String role) {
        String normalized = normalizeSystemRole(role);
        if ("merchant".equals(normalized)) {
            return "merchant";
        }
        return "consumer";
    }

    private String normalizeSystemRole(String role) {
        if (!StringUtils.hasText(role)) {
            return "consumer";
        }
        String normalized = role.trim().toLowerCase(Locale.ROOT);
        if ("merchant".equals(normalized) || "admin".equals(normalized) || "consumer".equals(normalized)) {
            return normalized;
        }
        return "consumer";
    }
}

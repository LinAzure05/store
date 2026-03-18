package com.crafts.platform.mapper;

import com.crafts.platform.entity.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {

    User findByUsername(@Param("username") String username);

    int insert(User user);
}

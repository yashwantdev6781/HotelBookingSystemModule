package com.ums.Service;

import com.ums.payload.LoginDto;
import com.ums.payload.UserDto;

public interface UserService {
    public UserDto addUser(UserDto userDto);


    String verifyLogin(LoginDto loginDto);
}

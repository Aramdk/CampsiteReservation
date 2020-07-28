package com.aram.demo.services;

import com.aram.demo.models.User;

public interface IUserService {

    User addUser(User user);

    User getUserByEmail(String email);

    void deleteUser(User user);
}

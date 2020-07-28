package com.aram.demo.services;

import com.aram.demo.models.User;
import com.aram.demo.utils.HibernateUtils;
import com.google.inject.Singleton;

@Singleton
public class UserService implements IUserService {

    @Override
    public User addUser(User user) {
       return (User) HibernateUtils.createEntity(user);
    }

    @Override
    public User getUserByEmail(String email) {
        return (User) HibernateUtils.getEntityByProperty(User.class, "email", email);
    }

    @Override
    public void deleteUser(User user) {
        HibernateUtils.deleteEntity(user);
    }
}

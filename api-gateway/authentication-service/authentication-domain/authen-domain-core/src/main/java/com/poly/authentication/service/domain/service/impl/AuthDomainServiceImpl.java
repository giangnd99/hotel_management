package com.poly.authentication.service.domain.service.impl;

import com.poly.authentication.service.domain.entity.Permission;
import com.poly.authentication.service.domain.entity.Role;
import com.poly.authentication.service.domain.entity.User;
import com.poly.authentication.service.domain.service.AuthDomainService;
import com.poly.domain.valueobject.UserId;

import java.util.UUID;

public class AuthDomainServiceImpl implements AuthDomainService {

    @Override
    public User initalizeAndValidUser(User user) {
        user.setId(new UserId(UUID.randomUUID()));
        return user;
    }

    @Override
    public User checkUser(User user) {
        return null;
    }

    @Override
    public User updateUser(User user) {
        return null;
    }

    @Override
    public User updateRole(User user, Role role) {
        return null;
    }

    @Override
    public User updatePermission(User user, Permission permission) {
        return null;
    }

    @Override
    public User addRole(User user, Role role) {
        return null;
    }

    @Override
    public User addPermission(User user, Permission permission) {
        return null;
    }

    @Override
    public User deleteRole(User user, Permission permission) {
        return null;
    }

    @Override
    public User deletePermission(User user) {
        return null;
    }
}

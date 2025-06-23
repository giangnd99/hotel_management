package com.poly.authentication.service.domain.service;

import com.poly.authentication.service.domain.entity.Permission;
import com.poly.authentication.service.domain.entity.Role;
import com.poly.authentication.service.domain.entity.User;

public interface AuthDomainService {

    User initalizeAndValidUser(User user);

    User checkUser(User user);

    User updateUser(User user);

    User updateRole(User user, Role role);

    User updatePermission(User user, Permission permission);

    User addRole(User user, Role role);

    User addPermission(User user, Permission permission);

    User deleteRole(User user, Permission permission);

    User deletePermission(User user);
}

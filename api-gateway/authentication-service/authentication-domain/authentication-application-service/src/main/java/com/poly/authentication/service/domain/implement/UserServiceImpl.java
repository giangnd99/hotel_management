package com.poly.authentication.service.domain.handler.user;

import com.poly.authentication.service.domain.port.out.repository.RoleRepository;
import com.poly.authentication.service.domain.port.out.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final PageUtil pageUtil;

    @Override
    public UserResponse createUser(UserCreationRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {

            throw new AppException(ErrorCode.USER_EXISTED);

        }
        User user = userMapper.toUser(request);

        String fullName = request.getLastName() + " " + request.getFirstName();

        user.setFullName(fullName);

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        User savedUser = userRepository.save(user);

        authorityRepository.save(Authority.builder().user(savedUser).role(roleRepository.findByName(PredefinedRole.USER)).build());

        return userMapper.toUserResponse(savedUser);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public Page<UserResponse> getUsers(int page) {

        Pageable pageable = pageUtil.createPageable(page);

        return userRepository.findAll(pageable).map(userMapper::toUserResponse);
    }

    @Override
    public UserResponse getMyInfo() {

        var context = SecurityContextHolder.getContext();

        String email = context.getAuthentication().getName();

        User user = userRepository.findByEmail(email)

                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        return userMapper.toUserResponse(user);
    }

    @Override
    @PostAuthorize("returnObject.email == authentication.name")
    public UserResponse getUserById(Integer id) {

        return userMapper.toUserResponse(userRepository.findById(id)

                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED)));

    }

    @Override
    @PostAuthorize("returnObject.email == authentication.name")
    public UserResponse updateUser(Integer userId, UserUpdatedRequest request) {

        User userUpdating = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        userMapper.updateUser(userUpdating, request);

        return userMapper.toUserResponse(userRepository.save(userUpdating));
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteUser(Integer userId) {
        userRepository.deleteById(userId);
    }

    @Override
    public void uploadAvatar(Integer userId, MultipartFile file) {

        var authUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findById(userId)

                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        if (!authUserEmail.equals(user.getEmail())) {

            log.error("Logged user is: {} try to upload avatar", authUserEmail);

            log.error("User email have permission is: {}", user.getEmail());

            throw new AppException(ErrorCode.AVATAR_NOT_PERMISSION);
        }

        String avatarUrl = uploadImageFileService.uploadImageFile(file);

        user.setAvatar(avatarUrl);

        userRepository.save(user);
    }

    @Override
    public void updatePassword(String email, String newPassword) {
        User userResetPassword = userRepository.findByEmail(email)

                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        String oldPassword = userResetPassword.getPassword();

        if (!passwordEncoder.matches(oldPassword, newPassword)) {

            userResetPassword.setPassword(passwordEncoder.encode(newPassword));

            userRepository.save(userResetPassword);

        } else throw new AppException(ErrorCode.PASSWORD_INVALID);
    }

}

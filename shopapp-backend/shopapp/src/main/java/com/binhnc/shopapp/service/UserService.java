package com.binhnc.shopapp.service;

import com.binhnc.shopapp.component.JwtTokenUtils;
import com.binhnc.shopapp.component.LocalizationUtils;
import com.binhnc.shopapp.dto.UpdateUserDTO;
import com.binhnc.shopapp.dto.UserDTO;
import com.binhnc.shopapp.exception.DataNotFoundException;
import com.binhnc.shopapp.exception.PermissionDenyException;
import com.binhnc.shopapp.model.Role;
import com.binhnc.shopapp.model.Token;
import com.binhnc.shopapp.model.User;
import com.binhnc.shopapp.repository.RoleRepository;
import com.binhnc.shopapp.repository.UserRepository;
import com.binhnc.shopapp.utils.MessageKeys;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtils jwtTokenUtils;
    private final AuthenticationManager authenticationManager;
    private final LocalizationUtils localizationUtils;

    @Override
    public User createUser(UserDTO userDTO) throws Exception {
        // Register user
        String phoneNumber = userDTO.getPhoneNumber();
        // Kiểm tra xem số điện thoại đã tồn tại hay chưa
        if (userRepository.existsByPhoneNumber(phoneNumber)) {
            throw new DataIntegrityViolationException("Phone number already exists");
        }
        Role role = roleRepository.findById(userDTO.getRoleId())
                .orElseThrow(() -> new DataNotFoundException(("Role not found")));
        if (role.getName().toLowerCase().equals(Role.ADMIN)) {
            throw new PermissionDenyException("You cannot register an admin account");
        }
        // Convert from userDTO => user
        User newUser = User.builder()
                .fullName(userDTO.getFullName())
                .phoneNumber(userDTO.getPhoneNumber())
                .password(userDTO.getPassword())
                .address(userDTO.getAddress())
                .dateOfBirth(userDTO.getDateOfBirth())
                .facebookAccountId(userDTO.getFacebookAccountId())
                .googleAccountId(userDTO.getGoogleAccountId())
                .build();
        newUser.setRole(role);
        // Kiểm tra nếu có accountId, không yêu cầu password --> Đăng nhập với google, facebook
        if (userDTO.getFacebookAccountId() == 0 && userDTO.getGoogleAccountId() == 0) {
            String password = userDTO.getPassword();
            String encodedPassword = passwordEncoder.encode(password);
            newUser.setPassword(encodedPassword);
        }
        return userRepository.save(newUser);
    }

    @Override
    public String login(String phoneNumber, String password, Long roleId) throws Exception {
        // spring security
        Optional<User> optionalUser = userRepository.findByPhoneNumber(phoneNumber);
        if (optionalUser.isEmpty()) {
            throw new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageKeys.WRONG_PHONE_PASSWORD));
        }
        // return optionalUser.get(); // Trả về JWT token?
        User existingUser = optionalUser.get();
        // Check password
        if (existingUser.getFacebookAccountId() == 0
                && existingUser.getGoogleAccountId() == 0) {
            if (!passwordEncoder.matches(password, existingUser.getPassword())) {
                throw new BadCredentialsException(localizationUtils.getLocalizedMessage(MessageKeys.WRONG_PHONE_PASSWORD));
            }
        }
        Optional<Role> optionalRole = roleRepository.findById(roleId);
        if (optionalRole.isEmpty() || !roleId.equals(existingUser.getRole().getId())) {
            throw new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageKeys.ROLE_DOES_NOT_EXISTS));
        }
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                phoneNumber, password, existingUser.getAuthorities()
        );
        // Authenticate with java spring security
        authenticationManager.authenticate(authenticationToken);
        return jwtTokenUtils.generateToken(existingUser);
    }

    @Override
    public User getUserDetailsFromToken(String token) throws Exception {
        if (jwtTokenUtils.isTokenExpired(token)) {
            throw new Exception("Token is expired");
        }
        String phoneNumber = jwtTokenUtils.extractPhoneNumber(token);
        Optional<User> user = userRepository.findByPhoneNumber(phoneNumber);
        if (user.isPresent()) {
            return user.get();
        } else {
            throw new Exception("User not found");
        }
    }

    @Transactional
    @Override
    public User updateUser(Long userId, UpdateUserDTO updateUserDTO) throws Exception {
        // Kiểm tra có user
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("User not found"));
        // Kiểm tra số điện thoại có trùng lặp?
        String newPhoneNumber = updateUserDTO.getPhoneNumber();
        if (!existingUser.getPhoneNumber().equals(newPhoneNumber)
                && userRepository.existsByPhoneNumber(newPhoneNumber)) {
            throw new DataIntegrityViolationException("Phone number already exist");
        }
        // Update
        if (updateUserDTO.getFullName() != null) {
            existingUser.setFullName(updateUserDTO.getFullName());
        }
        if (updateUserDTO.getPhoneNumber() != null) {
            existingUser.setPhoneNumber(updateUserDTO.getPhoneNumber());
        }
        if (updateUserDTO.getAddress() != null) {
            existingUser.setAddress(updateUserDTO.getAddress());
        }
        if (updateUserDTO.getDateOfBirth() != null) {
            existingUser.setDateOfBirth(updateUserDTO.getDateOfBirth());
        }
        if (updateUserDTO.getFacebookAccountId() > 0) {
            existingUser.setFacebookAccountId(updateUserDTO.getFacebookAccountId());
        }
        if (updateUserDTO.getGoogleAccountId() > 0) {
            existingUser.setGoogleAccountId(updateUserDTO.getGoogleAccountId());
        }

        //existingUser.setRole(roleUser);
        if (updateUserDTO.getPassword() != null
                && !updateUserDTO.getPassword().isEmpty()) {
            String newPassword = updateUserDTO.getPassword();
            String enCodedPassword = passwordEncoder.encode(newPassword);
            existingUser.setPassword(enCodedPassword);
        }
        return userRepository.save(existingUser);
    }

    @Override
    public Page<User> findAll(@Param("keyword") String keyword, Pageable pageable) throws Exception {
        return userRepository.findAll(keyword, pageable);
    }

    @Override
    @Transactional
    public void resetPassword(Long userId, String newPassword) throws Exception {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("User not found"));
        String encodedPassword = passwordEncoder.encode(newPassword);
        existingUser.setPassword(encodedPassword);
        userRepository.save(existingUser);
        // reset password => clear token
        //...
    }
}

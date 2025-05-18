package com.finance.user.serviceImpl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.finance.user.entity.UserEntity;
import com.finance.user.entity.UserInfoEntity;
import com.finance.user.model.User;
import com.finance.user.model.UserInfo;
import com.finance.user.repository.UserRepository;
import com.finance.user.repository.UserInfoRepository;
import com.finance.user.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserInfoRepository userInfoRepository;
    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    @Override
    public void registerUser(User registrationDTO) {

        if (userRepository.existsByUsername(registrationDTO.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        ObjectMapper modelMapper = new ObjectMapper();
        UserEntity userEntity = modelMapper.convertValue(registrationDTO, UserEntity.class);
        userEntity.setPassword(passwordEncoder.encode(registrationDTO.getPassword()));
        userRepository.save(userEntity);
    }

    @Override
    public void loginUser(User registrationDTO) {

    }

    @Override
    public User getUser(String username) {
        UserEntity userEntity =  userRepository.findByUsername(username);
        ObjectMapper modelMapper = new ObjectMapper();
       return modelMapper.convertValue(userEntity, User.class);
    }

    @Override
    @Transactional
    public void createUserInfo(UserInfo userInfo) {
        // First find the associated user
        UserEntity userEntity = userRepository.findByUsername(userInfo.getUsername());
        if (userEntity == null) {
            throw new RuntimeException("User not found");
        }

        // Create and map UserInfoEntity manually or configure ObjectMapper
        UserInfoEntity userInfoEntity = new UserInfoEntity();
        userInfoEntity.setFirstName(userInfo.getFirstName());
        userInfoEntity.setLastName(userInfo.getLastName());
        userInfoEntity.setEmail(userInfo.getEmail());
        userInfoEntity.setPhone(userInfo.getPhone());
        userInfoEntity.setAddress(userInfo.getAddress());
        userInfoEntity.setCity(userInfo.getCity());
        userInfoEntity.setSalary(userInfo.getSalary());
        userInfoEntity.setUser(userEntity);

        userInfoRepository.save(userInfoEntity);
    }

}
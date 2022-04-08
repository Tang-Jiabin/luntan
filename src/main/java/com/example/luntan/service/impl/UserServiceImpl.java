package com.example.luntan.service.impl;


import com.example.luntan.dao.UserRepository;
import com.example.luntan.dto.UserDTO;
import com.example.luntan.pojo.User;
import com.example.luntan.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void login(UserDTO userDTO) {

    }

    @Override
    public UserDTO wxLogin(UserDTO userDTO) {
        Optional<User> userOptional = userRepository.findByOpenid(userDTO.getOpenid());

        if (userOptional.isPresent()) {
            BeanUtils.copyProperties(userOptional.get(), userDTO);
        } else {
            User user = new User();
            BeanUtils.copyProperties(userDTO, user);
            user.setCtime(Instant.now());
            User save = userRepository.save(user);
            userDTO.setId(save.getId());
        }
        return userDTO;
    }

    @Override
    public UserDTO update(UserDTO userDTO) {
        Optional<User> userOptional = userRepository.findById(userDTO.getId());
        userOptional.ifPresent(user -> {
            BeanUtils.copyProperties(userDTO, user);
            User save = userRepository.save(user);
            BeanUtils.copyProperties(user, userDTO);
        });
        return userDTO;
    }

    @Override
    public List<UserDTO> findAllByIdList(List<Integer> uidList) {
        List<UserDTO> userDTOList = new ArrayList<>();
        List<User> allById = userRepository.findAllById(uidList);
        for (User user : allById) {
            UserDTO userDTO = new UserDTO();
            BeanUtils.copyProperties(user, userDTO);
            userDTOList.add(userDTO);
        }
        return userDTOList;
    }
}

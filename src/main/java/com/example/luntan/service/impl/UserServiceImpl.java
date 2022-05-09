package com.example.luntan.service.impl;


import com.example.luntan.dao.UserRepository;
import com.example.luntan.dto.UserDTO;
import com.example.luntan.pojo.User;
import com.example.luntan.service.UserService;
import com.example.luntan.vo.PageVO;
import com.example.luntan.vo.UserVO;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;


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
            user.setSign("暂无签名");
            user.setState(1);
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
            BeanUtils.copyProperties(save, userDTO);
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

    @Override
    public UserDTO findById(Integer uid) {
        UserDTO userDTO = new UserDTO();
        Optional<User> userOptional = userRepository.findById(uid);
        userOptional.ifPresent(user -> BeanUtils.copyProperties(user, userDTO));
        return userDTO;
    }

    @Override
    public Integer findCount() {
        return userRepository.findCount();
    }

    @Override
    public Page<User> findPage(Integer page, Integer limit, String nickname) {
        page = page <= 1 ? 0 : page - 1;
        Pageable pageable = PageRequest.of(page, limit, Sort.Direction.DESC, "ctime");
        return userRepository.findAll((root, query, criteriaBuilder) -> {
            List<Predicate> list = new ArrayList<>();

            if (StringUtils.hasText(nickname)) {
                list.add(criteriaBuilder.like(root.get("name").as(String.class), "%" + nickname + "%"));
            }

            Predicate[] p = new Predicate[list.size()];
            return criteriaBuilder.and(list.toArray(p));
        }, pageable);
    }

    @Override
    public PageVO<UserVO> page2VO(Page<User> userPage) {
        PageVO<UserVO> pageVO = new PageVO<UserVO>();
        BeanUtils.copyProperties(userPage, pageVO);
        return pageVO;
    }

    @Override
    public List<UserVO> list2VO(List<User> userList) {
        List<UserVO> userVOList = new ArrayList<>();
        for (User user : userList) {
            UserVO userVO = po2vo(user);
            userVOList.add(userVO);
        }
        return userVOList;
    }

    @Override
    public void del(Integer id) {
        userRepository.deleteById(id);
    }

    private UserVO po2vo(User user) {
        UserVO userVO = new UserVO();
        userVO.setId(user.getId());
        userVO.setName(user.getName());
        userVO.setSign(user.getSign());
        userVO.setLogo(user.getLogo());
        userVO.setCtime(user.getCtime());
        userVO.setSex(user.getSex());
        return userVO;
    }
}

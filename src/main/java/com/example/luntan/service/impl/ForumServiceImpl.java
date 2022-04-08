package com.example.luntan.service.impl;


import com.example.luntan.dao.DzRepository;
import com.example.luntan.dao.ForumRepository;
import com.example.luntan.dao.ScRepository;
import com.example.luntan.dto.ForumDTO;
import com.example.luntan.dto.UserDTO;
import com.example.luntan.pojo.Dz;
import com.example.luntan.pojo.Forum;
import com.example.luntan.pojo.Sc;
import com.example.luntan.service.ForumService;
import com.example.luntan.service.UserService;
import com.example.luntan.vo.ForumQueryVO;
import com.example.luntan.vo.ForumVO;
import com.example.luntan.vo.PageVO;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.data.domain.Page;

import javax.persistence.criteria.Predicate;
import java.util.Optional;
import java.util.stream.Collectors;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class ForumServiceImpl implements ForumService {

    private final ForumRepository forumRepository;
    private final UserService userService;
    private final DzRepository dzRepository;
    private final ScRepository scRepository;

    public ForumServiceImpl(ForumRepository forumRepository, UserService userService, DzRepository dzRepository, ScRepository scRepository) {
        this.forumRepository = forumRepository;
        this.userService = userService;
        this.dzRepository = dzRepository;
        this.scRepository = scRepository;
    }

    @Override
    public void add(ForumDTO forumDTO) {
        Forum forum = new Forum();
        BeanUtils.copyProperties(forumDTO, forum);
        forum.setCtime(Instant.now());
        forumRepository.save(forum);
    }

    @Override
    public PageVO<ForumVO> findPage(ForumQueryVO forumQueryVO) {

        int page = forumQueryVO.getPage() <= 1 ? 0 : forumQueryVO.getPage() - 1;
        Pageable pageable = null;

        switch (forumQueryVO.getLx()) {
            case 1:
                //TODO 推荐 按照推荐算法排序
                forumQueryVO.setLx(null);
                pageable = PageRequest.of(page, forumQueryVO.getLimit(), Sort.Direction.DESC, "ctime");
                break;
            case 2:
                //最新 按照发布时间排序

                pageable = PageRequest.of(page, forumQueryVO.getLimit(), Sort.Direction.DESC, "ctime");
                break;
            case 3:
                //排行 按照点赞数量排序
                forumQueryVO.setLx(null);
                pageable = PageRequest.of(page, forumQueryVO.getLimit(), Sort.Direction.DESC, "dzmun");
                break;
            default:
                //分类 按照更新时间排序

                pageable = PageRequest.of(page, forumQueryVO.getLimit(), Sort.Direction.DESC, "utime");
                break;
        }


        Page<Forum> forumPage = forumRepository.findAll((root, query, criteriaBuilder) -> {
            List<Predicate> list = new ArrayList<>();

            if (StringUtils.hasText(forumQueryVO.getTitle())) {
                list.add(criteriaBuilder.like(root.get("content").as(String.class), "%" + forumQueryVO.getTitle() + "%"));
            }

            if (forumQueryVO.getUid() != null) {
                list.add(criteriaBuilder.equal(root.get("uid").as(Integer.class), forumQueryVO.getUid()));
            }

            if (forumQueryVO.getLx() != null) {
                list.add(criteriaBuilder.equal(root.get("labelsid").as(Integer.class), forumQueryVO.getLx()));
            }

            Predicate[] p = new Predicate[list.size()];
            return criteriaBuilder.and(list.toArray(p));
        }, pageable);

        List<Integer> uidList = forumPage.get().map(Forum::getUid).collect(Collectors.toList());
        PageVO<ForumVO> pageVO = new PageVO<ForumVO>();
        BeanUtils.copyProperties(forumPage, pageVO);
        List<Forum> forumContent = forumPage.getContent();
        List<ForumVO> content = forumList2VO(forumContent);
        content = addUserInfo(content, uidList);
        content = addDzStatus(content, forumQueryVO.getUid());
        content = addScStatus(content, forumQueryVO.getUid());
        pageVO.setContent(content);

        return pageVO;
    }

    @Override
    public void sc(Integer uid, Integer id) {
        Optional<Sc> scOptional = scRepository.findByUidAndFid(uid, id);
        Optional<Forum> forumOptional = forumRepository.findById(id);
        scOptional.ifPresentOrElse(sc -> {
            scRepository.delete(sc);
            forumOptional.ifPresent(forum -> {
                forum.setScmun(forum.getScmun() - 1);
                forumRepository.save(forum);
            });}, () -> {
            Sc sc = new Sc();
            sc.setUid(uid);
            sc.setFid(id);
            scRepository.save(sc);
            forumOptional.ifPresent(forum -> {
                forum.setScmun(forum.getScmun() + 1);
                forumRepository.save(forum);
            });
        });
    }

    @Override
    public void dz(Integer uid, Integer id) {
        Optional<Dz> dzOptional = dzRepository.findByUidAndFid(uid, id);
        Optional<Forum> forumOptional = forumRepository.findById(id);
        dzOptional.ifPresentOrElse(dz -> {
            dzRepository.delete(dz);
            forumOptional.ifPresent(forum -> {
                forum.setDzmun(forum.getDzmun() - 1);
                forumRepository.save(forum);
            });
        }, () -> {
            Dz dz = new Dz();
            dz.setUid(uid);
            dz.setFid(id);
            dzRepository.save(dz);

            forumOptional.ifPresent(forum -> {
                forum.setDzmun(forum.getDzmun() + 1);
                forumRepository.save(forum);
            });
        });
    }

    private List<ForumVO> forumList2VO(List<Forum> forumContent) {
        List<ForumVO> forumVOList = new ArrayList<>();
        for (Forum forum : forumContent) {
            ForumVO forumVO = new ForumVO();
            BeanUtils.copyProperties(forum, forumVO);
            forumVOList.add(forumVO);
        }
        return forumVOList;
    }

    public List<ForumVO> addDzStatus(List<ForumVO> forumVOList, Integer uid) {
        List<Integer> fidList = dzRepository.findAllByUid(uid).stream().map(Dz::getFid).collect(Collectors.toList());
        forumVOList.forEach(forumVO -> {
            forumVO.setDz(0);
            if (fidList.contains(forumVO.getId())) {
                forumVO.setDz(1);
            }
        });
        return forumVOList;
    }

    public List<ForumVO> addScStatus(List<ForumVO> forumVOList, Integer uid) {
        List<Integer> fidList = scRepository.findAllByUid(uid).stream().map(Sc::getFid).collect(Collectors.toList());
        forumVOList.forEach(forumVO -> {
            forumVO.setSc(0);
            if (fidList.contains(forumVO.getId())) {
                forumVO.setSc(1);
            }
        });
        return forumVOList;
    }

    public List<ForumVO> addUserInfo(List<ForumVO> forumList, List<Integer> uidList) {

        List<UserDTO> userList = userService.findAllByIdList(uidList);
        List<ForumVO> forumVOList = new ArrayList<>();
        for (ForumVO forumVO : forumList) {
            for (UserDTO userDTO : userList) {
                if (userDTO.getId().equals(forumVO.getUid())) {
                    forumVO.setUname(userDTO.getName());
                    forumVO.setLogo(userDTO.getLogo());
                    forumVOList.add(forumVO);
                }
            }
        }
        return forumVOList;
    }
}

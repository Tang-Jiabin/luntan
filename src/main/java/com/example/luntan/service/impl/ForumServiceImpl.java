package com.example.luntan.service.impl;


import ch.qos.logback.core.joran.util.beans.BeanUtil;
import com.example.luntan.common.APIException;
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
    public Page<Forum> findPage(ForumQueryVO forumQueryVO) {

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
                forumQueryVO.setLx(null);
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


        return forumRepository.findAll((root, query, criteriaBuilder) -> {
            List<Predicate> list = new ArrayList<>();

            if (StringUtils.hasText(forumQueryVO.getTitle())) {
                list.add(criteriaBuilder.like(root.get("content").as(String.class), "%" + forumQueryVO.getTitle() + "%"));
            }

            if (forumQueryVO.getUid() != null && forumQueryVO.getUid() != 0) {
                list.add(criteriaBuilder.equal(root.get("uid").as(Integer.class), forumQueryVO.getUid()));
            }

            if (forumQueryVO.getLx() != null) {
                list.add(criteriaBuilder.equal(root.get("labelsid").as(Integer.class), forumQueryVO.getLx()));
            }

            Predicate[] p = new Predicate[list.size()];
            return criteriaBuilder.and(list.toArray(p));
        }, pageable);
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
            });
        }, () -> {
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

    @Override
    public ForumDTO findById(Integer id) {
        ForumDTO forumDTO = new ForumDTO();
        Optional<Forum> forumOptional = forumRepository.findById(id);
        forumOptional.ifPresentOrElse(forum -> BeanUtils.copyProperties(forum, forumDTO), () -> {
            throw new APIException(404, "帖子不存在");
        });
        return forumDTO;
    }

    @Override
    public ForumVO dto2vo(ForumDTO forumDTO, UserDTO userDTO, Integer loginId) {
        ForumVO forumVO = new ForumVO();
        BeanUtils.copyProperties(forumDTO, forumVO);
        forumVO.setLogo(userDTO.getLogo());
        forumVO.setUname(userDTO.getName());
        addDzInfo(forumVO, loginId);
        addScInfo(forumVO, loginId);
        return forumVO;
    }

    @Override
    public PageVO<ForumVO> page2VO(Page<Forum> page) {
        PageVO<ForumVO> pageVO = new PageVO<ForumVO>();
        BeanUtils.copyProperties(page, pageVO);
        return pageVO;
    }

    @Override
    public List<ForumVO> forumList2VO(List<Forum> forumList, List<UserDTO> userDTOList, Integer loginId) {
        List<ForumVO> forumVOList = new ArrayList<>();
        for (Forum forum : forumList) {
            ForumDTO forumDTO = po2dto(forum);
            for (UserDTO userDTO : userDTOList) {
                if (forum.getUid().equals(userDTO.getId())) {
                    ForumVO forumVO = dto2vo(forumDTO, userDTO, loginId);
                    forumVOList.add(forumVO);
                }
            }
        }
        return forumVOList;
    }

    private void addScInfo(ForumVO forumVO, Integer loginId) {
        forumVO.setSc(0);
        if (loginId != null) {
            Optional<Sc> scOptional = scRepository.findByUidAndFid(loginId, forumVO.getId());
            scOptional.ifPresent(sc -> {
                forumVO.setSc(1);
            });
        }
    }

    private void addDzInfo(ForumVO forumVO, Integer loginId) {
        forumVO.setDz(0);
        if (loginId != null) {
            Optional<Dz> dzOptional = dzRepository.findByUidAndFid(loginId, forumVO.getId());
            dzOptional.ifPresent(dz -> {
                forumVO.setDz(1);
            });
        }
    }

    private ForumDTO po2dto(Forum forum) {
        ForumDTO forumDTO = new ForumDTO();
        forumDTO.setCtime(forum.getCtime());
        forumDTO.setId(forum.getId());
        forumDTO.setUid(forum.getUid());
        forumDTO.setLabelsid(forum.getLabelsid());
        forumDTO.setUtime(forum.getUtime());
        forumDTO.setContent(forum.getContent());
        forumDTO.setPicture(forum.getPicture());
        forumDTO.setDzmun(forum.getDzmun());
        forumDTO.setPlmun(forum.getPlmun());
        forumDTO.setScmun(forum.getScmun());
        return forumDTO;
    }

}

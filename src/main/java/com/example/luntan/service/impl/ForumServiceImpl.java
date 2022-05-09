package com.example.luntan.service.impl;


import com.example.luntan.common.APIException;
import com.example.luntan.dao.*;
import com.example.luntan.dto.ForumDTO;
import com.example.luntan.dto.UserDTO;
import com.example.luntan.pojo.*;
import com.example.luntan.service.ForumService;
import com.example.luntan.service.Recommend;
import com.example.luntan.service.Similarity;
import com.example.luntan.vo.*;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class ForumServiceImpl implements ForumService {


    private final DzRepository dzRepository;
    private final ScRepository scRepository;
    private final PlRepository plRepository;
    private final JlRepository jlRepository;
    private final UserRepository userRepository;
    private final ForumRepository forumRepository;
    private final ReportRepository reportRepository;
    private final DataModelRepository dataModelRepository;


    @Override
    public void add(ForumDTO forumDTO) {
        Optional<User> userOptional = userRepository.findById(forumDTO.getUid());
        userOptional.ifPresent(user -> {
            if (user.getState()!=1) {
                throw new APIException(411,"已被禁言");
            }
        });
        Forum forum = new Forum();
        BeanUtils.copyProperties(forumDTO, forum);
        forum.setCtime(Instant.now());
        forum.setScore(0d);
        forum.setDzmun(0);
        forum.setScmun(0);
        forum.setPlmun(0);
        forumRepository.save(forum);
    }

    @Override
    public Page<Forum> findPage(ForumQueryVO forumQueryVO) {

        int page = forumQueryVO.getPage() <= 1 ? 0 : forumQueryVO.getPage() - 1;
        int limit = forumQueryVO.getLimit();
        Pageable pageable = null;

        switch (forumQueryVO.getLx() == null || forumQueryVO.getLx() == 0 ? 1 : forumQueryVO.getLx()) {
            case 1:
                List<DataModel> dataModelList = dataModelRepository.findAll();
                //计算相似度
                Similarity similarity = new PearsonCorrelationSimilarity(dataModelList);
                //构建基于用户的推荐系统
                Recommend recommend = new GenericUserBasedRecommender(dataModelList, similarity);
                //推荐
                List<Integer> fidList = recommend.recommendedBecause(forumQueryVO.getLoginId(), forumQueryVO.getPage(), forumQueryVO.getLimit());

                forumQueryVO.setLx(null);
                forumQueryVO.setIds(fidList);
                pageable = PageRequest.of(page, forumQueryVO.getLimit(), Sort.Direction.DESC, "ctime");
                break;
            case 2:
                //最新 按照发布时间排序
                forumQueryVO.setLx(null);
                pageable = PageRequest.of(page, forumQueryVO.getLimit(), Sort.Direction.DESC, "ctime");
                break;
            case 3:
                //排行 score=log5z+t/86400
                forumQueryVO.setLx(null);
                pageable = PageRequest.of(page, forumQueryVO.getLimit(), Sort.Direction.DESC, "score");
                break;
            default:
                //分类 按照更新时间排序
                pageable = PageRequest.of(page, forumQueryVO.getLimit(), Sort.Direction.DESC, "ctime");
                break;
        }

        return findPageAndSort(forumQueryVO, pageable);
    }

    @NotNull
    private Page<Forum> findPageAndSort(ForumQueryVO forumQueryVO, Pageable pageable) {
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

            if (forumQueryVO.getIds() != null) {
                CriteriaBuilder.In<Object> in = criteriaBuilder.in(root.get("id"));
                forumQueryVO.getIds().forEach(in::value);
                list.add(in);
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
            forumOptional.ifPresentOrElse(forum -> {
                forum.setScmun(forum.getScmun() - 1);
                forumRepository.save(forum);
            }, () -> {
                throw new APIException(404, "帖子不存在");
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
            forumOptional.ifPresent(forum -> {
                dz.setFUid(forum.getUid());
                forum.setDzmun(forum.getDzmun() + 1);
                forumRepository.save(forum);
            });
            dzRepository.save(dz);
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

    @Override
    public UserForumVO findUserDataStatistics(Integer uid) {
        UserForumVO userForumVO = new UserForumVO();
        Integer tiezi = forumRepository.findCountByUid(uid);
        Integer dz = dzRepository.findCountByFUid(uid);
        Integer pl = plRepository.findCountByUid(uid);
        userForumVO.setTiezi(tiezi);
        userForumVO.setDz(dz);
        userForumVO.setPl(pl);
        return userForumVO;
    }

    @Override
    public List<Sc> findScList(Integer uid) {
        return scRepository.findAllByUid(uid);
    }

    @Async
    @Override
    public void addJl(ItemIdVO itemIdVO) {
        if (itemIdVO.getUid() != null && itemIdVO.getId() != null) {
            Optional<Jl> jlOptional = jlRepository.findByFidAndUid(itemIdVO.getId(), itemIdVO.getUid());
            jlOptional.ifPresent(jlRepository::delete);
            Jl jl = new Jl();
            jl.setFid(itemIdVO.getId());
            jl.setUid(itemIdVO.getUid());
            jl.setCtime(Instant.now());
            jlRepository.save(jl);
        }
    }

    @Override
    public List<Jl> findJlList(Integer loginId) {
        return jlRepository.findAllByUid(loginId);
    }

    @Override
    public Integer findCount() {
        return forumRepository.findCount();
    }

    @Override
    public void del(Integer id) {
        List<Report> reportList = reportRepository.findAllByFid(id);
        for (Report report : reportList) {

            report.setState(2);
            reportRepository.save(report);

        }
        forumRepository.deleteById(id);
    }

    @Async
    @Override
    public void updateScore(Integer id) {
        Optional<Forum> forumOptional = forumRepository.findById(id);
        forumOptional.ifPresent(forum -> {
//            long epochSecond = LocalDateTime.of(2022, 4, 1, 0, 0, 0).toEpochSecond(ZoneOffset.of("+08:00"));
            long epochSecond = 1648742400;
            double score = Math.log(forum.getDzmun() + forum.getPlmun() + forum.getScmun()) / Math.log(5) + forum.getCtimeEpochSecond() - epochSecond / 86400d;
            forum.setScore(score);
            forumRepository.save(forum);
        });
    }

    @Override
    public void deljl(Integer id, Integer uid) {
        Optional<Jl> jlOptional = jlRepository.findByFidAndUid(id, uid);
        jlOptional.ifPresent(jlRepository::delete);
    }

    @Override
    public void report(PlAddVO plAddVO) {
        Report report = new Report();
        report.setFid(plAddVO.getId());
        report.setUid(plAddVO.getUid());
        report.setContent(plAddVO.getContent());
        report.setCtime(Instant.now());
        report.setState(1);
        reportRepository.save(report);
    }

    @Override
    public PageVO<ReportVO> findReportPage(ForumQueryVO forumQueryVO) {
        int page = forumQueryVO.getPage() <= 1 ? 0 : forumQueryVO.getPage() - 1;
        int limit = forumQueryVO.getLimit();
        Pageable pageable = PageRequest.of(page, limit, Sort.Direction.DESC, "ctime");
        Page<Report> reportPage = reportRepository.findAll((root, query, criteriaBuilder) -> {
            List<Predicate> list = new ArrayList<>();
            Predicate[] p = new Predicate[list.size()];
            return criteriaBuilder.and(list.toArray(p));
        }, pageable);
        PageVO<ReportVO> pageVO = new PageVO<>();
        BeanUtils.copyProperties(reportPage, pageVO);
        List<Report> content = reportPage.getContent();

        List<Forum> forumList = forumRepository.findAllById(content.stream().map(Report::getFid).collect(Collectors.toList()));
        List<User> userList = userRepository.findAllById(content.stream().map(Report::getUid).collect(Collectors.toList()));
        List<ReportVO> reportVOList = new ArrayList<>();
        ReportVO reportVO;
        for (Report report : content) {
            reportVO = new ReportVO();
            BeanUtils.copyProperties(report, reportVO);
            for (Forum forum : forumList) {
                if (report.getFid().equals(forum.getId())) {
                    reportVO.setForumContent(forum.getContent());
                    break;
                }
            }
            for (User user : userList) {
                if (report.getUid().equals(user.getId())) {
                    reportVO.setNickname(user.getName());
                    break;
                }
            }
            reportVOList.add(reportVO);
        }
        pageVO.setContent(reportVOList);
        return pageVO;
    }

    @Override
    public void ban(Integer id) {
        Optional<Forum> forumOptional = forumRepository.findById(id);
        forumOptional.ifPresent(forum -> {
            Optional<User> userOptional = userRepository.findById(forum.getUid());
            userOptional.ifPresent(user -> {
                user.setState(2);
                userRepository.save(user);
            });
            List<Report> reportList = reportRepository.findAllByFid(forum.getId());
            for (Report report : reportList) {
                if (report.getUid().equals(forum.getUid())) {
                    report.setState(2);
                    reportRepository.save(report);
                }
            }
        });


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

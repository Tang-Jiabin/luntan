package com.example.luntan.service.impl;

import com.example.luntan.dao.PlRepository;
import com.example.luntan.dto.UserDTO;
import com.example.luntan.pojo.Pl;
import com.example.luntan.service.PlService;
import com.example.luntan.vo.PageVO;
import com.example.luntan.vo.PlAddVO;
import com.example.luntan.vo.PlQueryVO;
import com.example.luntan.vo.PlVO;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PlServiceImpl implements PlService {

    private PlRepository plRepository;


    @Override
    public void add(PlAddVO plAddVO) {
        Optional<Pl> plOptional = plRepository.findFirstByFidAndPidOrderByStoreyDesc(plAddVO.getId(), plAddVO.getPid());
        int storey = 1;
        if (plOptional.isPresent()) {
            storey = plOptional.get().getStorey() + 1;
        }
        Pl pl = new Pl();
        pl.setPid(plAddVO.getPid());
        pl.setFid(plAddVO.getId());
        pl.setUid(plAddVO.getUid());
        pl.setStorey(storey);
        pl.setContent(plAddVO.getContent());
        pl.setCtime(Instant.now());
        plRepository.save(pl);

    }

    @Override
    public Page<Pl> findPage(PlQueryVO plQueryVO) {
        int page = plQueryVO.getPage()==null||plQueryVO.getPage() <= 1 ? 0 : plQueryVO.getPage() - 1;
        Pageable pageable = PageRequest.of(page, plQueryVO.getLimit(), Sort.Direction.ASC, "storey");
        return plRepository.findAll((root, query, criteriaBuilder) -> {
            List<Predicate> list = new ArrayList<>();

            list.add(criteriaBuilder.equal(root.get("fid").as(Integer.class), plQueryVO.getFid()));

            Predicate[] p = new Predicate[list.size()];
            return criteriaBuilder.and(list.toArray(p));
        }, pageable);
    }

    @Override
    public PageVO<PlVO> page2vo(Page<Pl> plPage) {
        List<Pl> plList = plPage.getContent();
        PageVO<PlVO> plPageVO = new PageVO<>();
        BeanUtils.copyProperties(plPage, plPageVO);
        List<PlVO> plVOList = poList2vo(plList);
        plPageVO.setContent(plVOList);
        return plPageVO;
    }

    @Override
    public List<PlVO> plAddUserInfo(List<PlVO> content, List<UserDTO> userDTOList) {
        for (PlVO plVO : content) {
            for (UserDTO userDTO : userDTOList) {
                if (plVO.getUid().equals(userDTO.getId())) {
                    plVO.setLogo(userDTO.getLogo());
                    plVO.setUname(userDTO.getName());
                }
            }
        }
        return content;
    }

    private List<PlVO> poList2vo(List<Pl> plList) {
        List<PlVO> plVOList = new ArrayList<>();
        for (Pl pl : plList) {
            plVOList.add(po2vo(pl));
        }
        return plVOList;
    }

    private PlVO po2vo(Pl pl) {
        PlVO plVO = new PlVO();
        plVO.setContent(pl.getContent());
        plVO.setId(pl.getId());
        plVO.setPid(pl.getPid());
        plVO.setFid(pl.getFid());
        plVO.setUid(pl.getUid());
        plVO.setStorey(String.format("第%d楼", pl.getStorey()));
        plVO.setContent(pl.getContent());
        plVO.setCtime(pl.getCtime());
        return plVO;
    }
}

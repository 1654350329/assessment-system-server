package com.tree.clouds.assessment.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tree.clouds.assessment.mapper.MatterListMapper;
import com.tree.clouds.assessment.model.entity.MatterList;
import com.tree.clouds.assessment.model.entity.RoleManage;
import com.tree.clouds.assessment.model.vo.IndicatorReportVO;
import com.tree.clouds.assessment.model.vo.PageParam;
import com.tree.clouds.assessment.service.MatterListService;
import com.tree.clouds.assessment.service.RoleManageService;
import com.tree.clouds.assessment.utils.LoginUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 待办l列表 服务实现类
 * </p>
 *
 * @author LZK
 * @since 2022-05-18
 */
@Service
public class MatterListServiceImpl extends ServiceImpl<MatterListMapper, MatterList> implements MatterListService {

    @Autowired
    private RoleManageService roleManageService;

    @Override
    public IPage<MatterList> matterListPage(PageParam pageVO) {
        List<RoleManage> roleManages = roleManageService.getRoleByUserId(LoginUserUtil.getUserId());
        List<String> roleCodes = roleManages.stream().map(RoleManage::getRoleCode).collect(Collectors.toList());
        Set<MatterList> matterLists=new LinkedHashSet<>();
        //获得管理员待办
        if (roleCodes.contains("ROLE_admin")) {
            List<MatterList> matterListIPage = this.baseMapper.getMatterList( 1, null,null);
            matterLists.addAll(matterListIPage);
        }
        //获得普通账号待办0.2
        if (roleCodes.contains("ROLE_up_user")) {
            List<MatterList> matterListIPage = this.baseMapper.getMatterList( 2,null, LoginUserUtil.getUnitId());
            matterLists.addAll(matterListIPage);
        }
        //获得账号管理员待办
        if (roleCodes.contains("ROLE_user_admin")) {
            List<MatterList> matterListIPage = this.baseMapper.getMatterList( 1,null, LoginUserUtil.getUnitId());
            matterLists.addAll(matterListIPage);
        }
        //获得账号管理员待办3 4
        if (roleCodes.contains("ROLE_EXPERT")) {
            List<MatterList> matterListIPage = this.baseMapper.getMatterList( 3,LoginUserUtil.getUserId(), null);
            matterLists.addAll(matterListIPage);
        }
        IPage<MatterList> iPage = new Page();
        iPage.setCurrent(pageVO.getCurrent());
        iPage.setSize(pageVO.getSize());
        int cursor = Math.toIntExact(((pageVO.getCurrent() - 1) * pageVO.getSize()));
        int limit = Math.toIntExact(pageVO.getSize());
        List<MatterList> paging = paging(cursor, limit, new ArrayList<>(matterLists));
        iPage.setRecords(paging);
        iPage.setTotal(matterLists.size());
        return iPage;
    }

    //手动实现分页
    public List<MatterList> paging(int cursor, int limit, List<MatterList> list) {
        //手动实现分页
        if (cursor < 0 || cursor >= list.size() || limit <= 0) {
            return null;
        }
        int lastIndex = cursor + limit;
        if (lastIndex > list.size()) {
            lastIndex = list.size();
        }
        //获得分页后的deviceIdList
        list = list.subList(cursor, lastIndex);
        return list;
    }

    @Override
    public void addMatter(String title, String unitId, String reportId, String userId, Integer type,String year,String indicatorsId) {
        MatterList matterList = new MatterList();
        matterList.setTitle(title);
        matterList.setUnitId(unitId);
        matterList.setReportId(reportId);
        matterList.setUserId(userId);
        matterList.setMatterType(type);
        matterList.setMatterStatus(0);
        matterList.setYear(year);
        matterList.setIndicatorsId(indicatorsId);
        this.save(matterList);
    }

    @Override
    public void updateMatter(String matterId) {
        MatterList matterList = new MatterList();
        matterList.setMatterStatus(1);
        this.updateById(matterList);
    }
}

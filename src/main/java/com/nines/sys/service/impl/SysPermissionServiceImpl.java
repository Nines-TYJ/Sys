package com.nines.sys.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nines.sys.entity.SysPermission;
import com.nines.sys.enums.StatusEnum;
import com.nines.sys.mapper.SysPermissionMapper;
import com.nines.sys.service.ISysPermissionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nines.sys.util.MyEnumUtil;
import com.nines.sys.vo.PageVo;
import com.nines.sys.vo.TreeNode;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 权限表 服务实现类
 * </p>
 *
 * @author Nines
 * @since 2020-10-21
 */
@Service
public class SysPermissionServiceImpl extends ServiceImpl<SysPermissionMapper, SysPermission> implements ISysPermissionService {

    @Override
    public boolean addPermission(SysPermission permission) {
        if (StrUtil.hasBlank(permission.getParentId())){
            return false;
        }
        if (StrUtil.hasBlank(permission.getName())){
            return false;
        }
        if (StrUtil.hasBlank(permission.getPermCode())){
            return false;
        }
        permission.setCreateTime(LocalDateTime.now());
        permission.setUpdateTime(LocalDateTime.now());
        return this.baseMapper.insert(permission) > 0;
    }

    @Override
    public boolean updatePermission(SysPermission permission) {
        SysPermission updatePermission = this.baseMapper.selectById(permission.getId());
        // 不为空且不相等才更新
        if (!StrUtil.hasBlank(permission.getParentId()) && !updatePermission.getParentId().equals(permission.getParentId())){
            updatePermission.setParentId(permission.getParentId());
        }
        if (!StrUtil.hasBlank(permission.getRemark()) && !updatePermission.getRemark().equals(permission.getRemark())){
            updatePermission.setRemark(permission.getRemark());
        }
        if (!updatePermission.getSort().equals(permission.getSort())){
            updatePermission.setSort(permission.getSort());
        }
        if (MyEnumUtil.isInclude(StatusEnum.class, permission.getStatus()) && !updatePermission.getStatus().equals(permission.getStatus())){
            updatePermission.setStatus(permission.getStatus());
        }
        updatePermission.setUpdateTime(LocalDateTime.now());
        return false;
    }

    @Override
    public boolean deletePermissionById(String id) {
        return false;
    }

    @Override
    public SysPermission findOneById(String id) {
        return this.baseMapper.selectById(id);
    }

    @Override
    public Map<String, Object> findPage(PageVo pageVo) {
        Page<SysPermission> page = new Page<>(pageVo.getPage(), pageVo.getSize());
        QueryWrapper<SysPermission> queryWrapper = new QueryWrapper<>();
        if (!StrUtil.hasBlank(pageVo.getName())){
            queryWrapper.like("name", pageVo.getName());
        }
        IPage<SysPermission> iPage = this.baseMapper.selectPage(page, queryWrapper);
        Map<String, Object> dataPage = new HashMap<>();
        dataPage.put("datas", iPage.getRecords());
        dataPage.put("totals", iPage.getTotal());
        return dataPage;
    }

    @Override
    public List<TreeNode> createTree() {
        List<SysPermission> permissionList = this.baseMapper.selectList(new QueryWrapper<SysPermission>().lambda()
                .eq(SysPermission::getParentId, "0")
                .eq(SysPermission::getStatus, StatusEnum.NORMAL));
        return addNodeToTree(permissionList);
    }

    /**
     * 递归添加树节点
     * @param permissionList 权限列表
     * @return 树
     */
    private List<TreeNode> addNodeToTree(List<SysPermission> permissionList){
        List<TreeNode> tree = new ArrayList<>();
        permissionList.forEach(permission -> {
            // 查询当前节点是否还有子节点
            List<SysPermission> childrenPermissionList = findPermissionListByParentId(permission.getId());
            if (childrenPermissionList.size() > 0){
                List<TreeNode> childrenTree = addNodeToTree(childrenPermissionList);
                tree.add(new TreeNode(permission.getId(), permission.getName(), childrenTree));
            }else {
                tree.add(new TreeNode(permission.getId(), permission.getName(), null));
            }
        });
        return tree;
    }

    /**
     * 通过父级id找到权限列表
     * @param id 父级id
     * @return 权限列表
     */
    private List<SysPermission> findPermissionListByParentId(String id){
        return this.baseMapper.selectList(new QueryWrapper<SysPermission>().lambda()
                .eq(SysPermission::getParentId, id)
                .eq(SysPermission::getStatus, StatusEnum.NORMAL)
                .orderByAsc(SysPermission::getSort));
    }
}

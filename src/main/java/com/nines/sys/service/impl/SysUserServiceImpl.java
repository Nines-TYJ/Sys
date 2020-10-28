package com.nines.sys.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nines.sys.entity.SysUser;
import com.nines.sys.entity.SysUserRole;
import com.nines.sys.enums.StatusEnum;
import com.nines.sys.mapper.SysUserMapper;
import com.nines.sys.mapper.SysUserRoleMapper;
import com.nines.sys.service.ISysUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nines.sys.util.MyEnumUtil;
import com.nines.sys.util.PasswordMd5Util;
import com.nines.sys.vo.PageVo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户 服务实现类
 * </p>
 *
 * @author Nines
 * @since 2020-10-22
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {

    @Resource
    private SysUserRoleMapper userRoleMapper;

    @Override
    public SysUser getUserByUsername(String username) {
        return this.baseMapper.selectOne(new QueryWrapper<SysUser>().lambda()
//                .select(SysUser::getPassWord, SysUser::getStatus)
                .eq(SysUser::getUserName, username));
    }

    @Override
    public List<String> getRoleByUsername(String username) {
        return this.baseMapper.getRoleByUsername(username);
    }

    @Override
    public List<String> getPermCodeByUsername(String username) {
        return this.baseMapper.getPermCodeByUsername(username);
    }

    @Override
    public boolean addUser(SysUser user) {
        // 昵称为空时直接设置为用户名
        if (StrUtil.hasBlank(user.getNickName())){
            user.setNickName(user.getUserName());
        }
        // 设置密码和盐
        Map<String, String> map = PasswordMd5Util.encryption(user.getPassWord());
        user.setPassWord(map.get("encryptPassword"));
        user.setSalt(map.get("salt"));
        // 设置数据创建时间
        LocalDateTime dataTime = LocalDateTime.now();
        user.setCreateTime(dataTime);
        user.setUpdateTime(dataTime);
        return this.baseMapper.insert(user) > 0;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteUserById(String id) {
        SysUser user = this.baseMapper.selectById(id);
        // 用户不存在
        if (user == null){
            return false;
        }
        // 用户删除成功
        if (this.baseMapper.deleteById(id) > 0){
            // 删除用户角色中关联的角色
            userRoleMapper.delete(new QueryWrapper<SysUserRole>().lambda().eq(SysUserRole::getUserId, id));
            return true;
        }
        return false;
    }

    @Override
    public boolean updateUser(SysUser user) {
        // 查出原来数据，在原来数据的基础上修改，避免重要字段被恶意修改
        SysUser updateUser = this.baseMapper.selectById(user.getId());
        // 不为空且不相等才更新
        if (!StrUtil.hasBlank(user.getNickName()) && !updateUser.getNickName().equals(user.getNickName())){
            updateUser.setNickName(user.getNickName());
        }
        // 邮箱不为空
        if (!StrUtil.hasBlank(user.getEmail()) && !updateUser.getEmail().equals(user.getEmail())){
            updateUser.setEmail(user.getEmail());
        }
        // 修改状态 先判断状态是否在enum中
        if (MyEnumUtil.isInclude(StatusEnum.class, user.getStatus()) && !updateUser.getStatus().equals(user.getStatus())){
            updateUser.setStatus(user.getStatus());
        }
        // 更新时间
        updateUser.setUpdateTime(LocalDateTime.now());
        return this.baseMapper.updateById(updateUser) > 0;
    }

    @Override
    public SysUser findOneById(String id) {
        return this.baseMapper.selectById(id);
    }

    @Override
    public Map<String, Object> findPage(PageVo pageVo) {
        Page<SysUser> page = new Page<>(pageVo.getPage(), pageVo.getSize());
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
        if (!StrUtil.hasBlank(pageVo.getName())){
            queryWrapper.like("name", pageVo.getName());
        }
        IPage<SysUser> iPage = this.baseMapper.selectPage(page, queryWrapper);
        Map<String, Object> dataPage = new HashMap<>();
        dataPage.put("datas", iPage.getRecords());
        dataPage.put("totals", iPage.getTotal());
        return dataPage;
    }

}

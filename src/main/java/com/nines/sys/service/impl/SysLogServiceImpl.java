package com.nines.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nines.sys.entity.SysLog;
import com.nines.sys.mapper.SysLogMapper;
import com.nines.sys.service.ISysLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nines.sys.util.PageUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * <p>
 * 日志表 服务实现类
 * </p>
 *
 * @author Nines
 * @since 2020-11-11
 */
@Service
public class SysLogServiceImpl extends ServiceImpl<SysLogMapper, SysLog> implements ISysLogService {

    @Override
    public boolean add(SysLog log) {
        log.setOperateTime(LocalDateTime.now());
        return this.baseMapper.insert(log) > 0;
    }

    @Override
    public PageUtil queryPage(PageUtil pageUtil) {
        Page<SysLog> page = new Page<>(pageUtil.getCurrent(), pageUtil.getSize());
        IPage<SysLog> iPage = this.baseMapper.selectPage(page, new QueryWrapper<SysLog>().lambda()
                .orderByDesc(SysLog::getOperateTime)
        );
        return new PageUtil(iPage);
    }
}

package com.nines.sys.service;

import com.nines.sys.entity.SysLog;
import com.baomidou.mybatisplus.extension.service.IService;
import com.nines.sys.util.PageUtil;

/**
 * <p>
 * 日志表 服务类
 * </p>
 *
 * @author Nines
 * @since 2020-11-11
 */
public interface ISysLogService extends IService<SysLog> {

    boolean add(SysLog log);

    /**
     * 分页
     * @param pageUtil 分页工具
     * @return 分页列表
     */
    PageUtil queryPage(PageUtil pageUtil);

}

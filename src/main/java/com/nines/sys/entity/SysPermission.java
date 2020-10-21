package com.nines.sys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 权限表
 * </p>
 *
 * @author Nines
 * @since 2020-10-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SysPermission implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    /**
     * 父级ID
     */
    private String parentId;

    /**
     * 权限名称
     */
    private String name;

    /**
     * 备注
     */
    private String remark;

    /**
     * 类型 0、菜单 1、功能
     */
    private Boolean type;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 链接地址
     */
    private String url;

    /**
     * 权限编码
     */
    private String permCode;

    /**
     * 0是启用,1是禁用
     */
    private Boolean status;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;


}

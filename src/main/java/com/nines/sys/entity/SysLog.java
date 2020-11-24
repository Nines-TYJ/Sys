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
 * 日志表
 * </p>
 *
 * @author Nines
 * @since 2020-11-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SysLog implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    /**
     * ip地址
     */
    private String ip;

    /**
     * 请求的访问地址
     */
    private String operateUrl;

    /**
     * 请求的方法
     */
    private String operateMethod;

    /**
     * 请求的浏览器
     */
    private String operateBy;

    /**
     * 请求的时间
     */
    private LocalDateTime operateTime;

}

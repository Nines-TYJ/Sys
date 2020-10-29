package com.nines.sys.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author TYJ
 * @date 2020/10/27 15:24
 */
@Data
public class PageVo implements Serializable {

    private String name;

    /**
     * 当前页面
     */
    private int page;

    /**
     * 页面大小
     */
    private int size;

}

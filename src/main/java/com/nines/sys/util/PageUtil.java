package com.nines.sys.util;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author TYJ
 * @date 2020/10/27 15:24
 */
@Data
public class PageUtil implements Serializable {

    private String searchText;

    /**
     * 当前页数
     */
    private int current;

    /**
     * 每页数据条数
     */
    private int size;

    /**
     * 数据总条数
     */
    private int total;

    /**
     * 数据列表
     */
    private List<?> records;

    public PageUtil() {
    }

    public PageUtil(String searchText, int current, int size) {
        this.searchText = searchText;
        this.current = current;
        this.size = size;
    }

    public PageUtil(IPage<?> iPage) {
        this.current = (int) iPage.getCurrent();
        this.size = (int) iPage.getSize();
        this.total = (int) iPage.getTotal();
        this.records = iPage.getRecords();
    }
}

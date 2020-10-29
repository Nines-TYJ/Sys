package com.nines.sys.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 菜单权限树
 * @author TYJ
 * @date 2020/10/29 10:49
 */
@Data
public class MenuTreeNodeVo<T> implements Serializable {

    /**
     * 节点名称
     */
    private String label;

    /**
     * 子节点
     */
    private List<T> children;

    public MenuTreeNodeVo() {
    }

    public MenuTreeNodeVo(String label, List<T> children) {
        this.label = label;
        this.children = children;
    }
}

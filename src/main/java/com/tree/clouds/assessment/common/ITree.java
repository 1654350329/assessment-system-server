package com.tree.clouds.assessment.common;

import java.util.List;

public interface ITree<T> {

    /**
     * 获取id
     *
     * @return
     */
    String getId();

    /**
     * 获取父id
     *
     * @return
     */
    String getParentId();

    /**
     * 获取孩子
     *
     * @return
     */
    List<T> getChildren();

    /**
     * 设置孩子
     *
     * @param children
     */
    void setChildren(List<T> children);
}

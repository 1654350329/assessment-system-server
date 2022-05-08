package com.tree.clouds.assessment.utils;

import cn.hutool.core.collection.CollUtil;
import com.tree.clouds.assessment.common.ITree;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ConvertUtil {
	/**
     * 集合转二叉树
     *
     * @param resource  来源集合
     * @param predicate 根节点标识判断
     * @return
     */
    public static <T extends ITree> List<T> convertTree(List<T> resource, Predicate<? super T> predicate) {
        List<T> result = resource.stream().filter(predicate).collect(Collectors.toList());
        // 移出父级元素
        resource.removeIf(result::contains);
        ConvertUtil.setChilder(result, resource);
        return result;
    }

    /**
     * 设置孩子
     *
     * @param parentList 根节点集合
     * @param elements 来源集合
     * @param <T>
     */
    private static <T extends ITree> void setChilder(List<T> parentList, List<T> elements) {
        if (CollUtil.isEmpty(elements)) {
            return;
        }
        // 遍历父级
        parentList.forEach(parent -> {
            List<T> childer = elements.stream().filter(element -> parent.getId().equals(element.getParentId())).collect(Collectors.toList());
            parent.setChildren(childer);
            elements.removeIf(childer::contains);
            setChilder(childer, elements);
        });
    }
}

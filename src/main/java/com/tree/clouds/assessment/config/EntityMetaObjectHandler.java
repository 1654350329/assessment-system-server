package com.tree.clouds.assessment.config;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.tree.clouds.assessment.utils.LoginUserUtil;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;

/**
 * mybatis-plus自动置值
 */
@Component
public class EntityMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        this.setFieldValByName("del", 0, metaObject);
        this.setFieldValByName("createdUser", LoginUserUtil.getUserId(), metaObject);
        this.setFieldValByName("createdTime", DateUtil.now(), metaObject);
        this.setFieldValByName("updatedUser", LoginUserUtil.getUserId(), metaObject);
        this.setFieldValByName("updatedTime", DateUtil.now(), metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("updatedUser", LoginUserUtil.getUserId(), metaObject);
        this.setFieldValByName("updatedTime", DateUtil.now(), metaObject);
    }
}

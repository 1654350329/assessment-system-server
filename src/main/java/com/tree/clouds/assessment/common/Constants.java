package com.tree.clouds.assessment.common;

import java.io.File;

public class Constants {
    public static final String TMP_HOME = System.getProperty("java.io.tmpdir") + File.separator;
    public static final String FILE_PATH = "D:\\file\\";

    public static final String ERROR_LOGIN = "ERROR_LOGIN";
    public static final String LOCK_ACCOUNT = "LOCK_ACCOUNT";
    /**
     * 存储用户登入,第二次登入 作对比
     */
    public static final String ACCOUNT_KEY = "ACCOUNT_KEY";
}

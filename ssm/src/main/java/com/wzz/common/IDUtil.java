package com.wzz.common;

import java.util.UUID;

/**
 * 各种ID工具类
 * @ClassName IDUtil
 * @Description TODO
 * @Author AZhen
 * Version 1.0
 **/
public class IDUtil {

    /**
     * 获取uuid（以去掉'-'字符）
     * @Author AZhen
     * @Param []
     * @return java.lang.String
     **/
    public static String getUUID(){
        return UUID.randomUUID().toString().replace("-", "");
    }
}
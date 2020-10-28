package com.nines.sys.util;

import cn.hutool.core.util.EnumUtil;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author TYJ
 * @date 2020/10/28 8:46
 */
@Slf4j
public class MyEnumUtil {

    /**
     * 判断某个枚举是否包含某个code值
     * @param enumClass 需要判断是否存在那个枚举中
     * @param code 需要判断的值
     * @return 包含返回true,否则返回false
     */
    public static boolean isInclude(Class enumClass, int code) {
        if (!enumClass.isEnum()){
            return false;
        }
        Object[] enumConstants = enumClass.getEnumConstants();
        try {
            Method getCode = enumClass.getMethod("getCode");
            for (Object enumConstant : enumConstants) {
                if (getCode.invoke(enumConstant).equals(code)){
                    return true;
                }
            }
        } catch (ReflectiveOperationException e) {
            log.error("MyEnumUtil：反射操作异常");
        }
        return false;
    }

}

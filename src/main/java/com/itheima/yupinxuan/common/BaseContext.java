package com.itheima.yupinxuan.common;

public class BaseContext {
    private static ThreadLocal<Long> threadLocal=new ThreadLocal<>();


    public static Long getCurrectId() {
        return threadLocal.get();
    }

    public static void setCurrectId(Long currectId) {
        threadLocal.set(currectId);
    }
}

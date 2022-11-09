package com.devmeng.baselib.entities;

import com.devmeng.baselib.utils.NetType;

import java.lang.reflect.Method;

/**
 * Created by Richard
 * Version : 1
 * Description :
 */
public class MethodManager {

    public Class<?> type;
    public NetType netType;
    public Method method;

    public MethodManager(Class<?> type, NetType netType, Method method) {
        this.type = type;
        this.netType = netType;
        this.method = method;
    }
}

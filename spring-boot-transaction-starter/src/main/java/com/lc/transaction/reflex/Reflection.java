package com.lc.transaction.reflex;

import java.lang.reflect.Method;

public class Reflection {

    /**
     * 根据路径实例化一个类
     *
     * @param path
     * @return
     * @throws Exception
     */
    public static Object newInstance(String path) throws Exception {
        Class clz = ReflexUtils.loaderClass(path);
        return clz.newInstance();
    }

    /**
     * 根据class实例化一个类
     *
     * @param clz
     * @return
     * @throws Exception
     */
    public static Object newInstance(Class clz) throws Exception {
        return clz.newInstance();
    }

    /**
     * 根据名称寻找方法执行
     *
     * @param las
     * @param methodName
     * @return
     * @throws Exception
     */
    public static Method getMethod(Class<?> las, String methodName) throws Exception {
        Method[] methods = las.getDeclaredMethods();
        Method method = null;
        // 方法筛选
        for (Method m : methods) {
            if (m.getName().equals(methodName)) {
                method = m;
                break;
            }
        }
        return method;
    }

    /**
     * 代执行方法
     *
     * @param method_name 方法名
     * @param las         类名
     * @return 返回方法执行后的对象obj
     */
    public static Object invoke(String method_name, Class<?> las) throws Exception {
        Method method = getMethod(las, method_name);
        return method.invoke(las.newInstance(), method.getParameters());
    }

    /**
     * 进行代理执行
     *
     * @param obj    执行对象
     * @param method 执行方法
     * @return 返回执行结果
     */
    public static Object invokeMethod(Object obj, Method method) throws Exception {
        return method.invoke(obj, (Object[]) method.getParameters());
    }

    /**
     * 进行代理执行
     *
     * @param las
     * @param method
     * @return
     */
    public static Object invokeMethod(Object obj, Method method, Object... args) throws Exception {
        return method.invoke(obj, args);
    }
}

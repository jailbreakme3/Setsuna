package com.jailbreakme3.setsuna

import okhttp3.ResponseBody
import java.lang.reflect.Type

/**
 * 将 ResponseBody 转换为相应的实体类
 */
interface ResponseBodyConverter {

    /**
     * 将 ResponseBody 反序列化为 [type] 类型的实体类
     *
     * @param response OkHttp 网络请求返回的 Response
     * @param type 想要反序列化后的类型
     *
     * @return 序列化后的实体类
     */
    fun <T> convert(response: ResponseBody, type: Type): T
}

package com.jailbreakme3.setsuna.setsuna


import com.jailbreakme3.setsuna.GsonConverter
import com.jailbreakme3.setsuna.ResponseBodyConverter
import com.jailbreakme3.setsuna.setsuna.SetsunaContext.defaultOkHttpClient
import com.jailbreakme3.setsuna.bean.CommonTokenBean
import okhttp3.OkHttpClient
import okhttp3.Response

/**
 * DSL initialization class
 * 用于组件初始化的DSL类
 */
class SetsunaBuildScope {

    internal var okHttpClientLazy: () -> OkHttpClient = {
        defaultOkHttpClient
    }

    internal var baseUrlLazy: () -> String = { "" }
    internal var responseBodyConverterLazy: () -> ResponseBodyConverter = { GsonConverter.create() }

    /**
     * 设置 OkHttpClient 实例
     * optional，如未调用，则使用默认client
     */
    fun okHttpClient(client: () -> OkHttpClient) {
        okHttpClientLazy = client
    }

    /**
     * 设置全局baseUrl
     * optional
     */
    fun baseUrl(url: String) {
        baseUrlLazy = { url }
    }

    /**
     * 设置反序列化工具，默认提供Gson
     */
    fun converter(converter: () -> ResponseBodyConverter) {
        responseBodyConverterLazy = converter
    }

}
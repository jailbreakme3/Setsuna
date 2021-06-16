package com.jailbreakme3.setsuna.setsuna

import com.jailbreakme3.setsuna.GsonConverter
import com.jailbreakme3.setsuna.ResponseBodyConverter
import com.jailbreakme3.setsuna.bean.CommonTokenBean
import okhttp3.OkHttpClient
import okhttp3.Response
import java.util.concurrent.TimeUnit

/**
 * To save configuration
 * 单例，保存组件配置
 */
object SetsunaContext {

    private const val DEFAULT_TIME_OUT = 30L

    internal var baseUrl: String = ""
    internal var converter: ResponseBodyConverter = GsonConverter.create()

    private const val SETSUNA_CLIENT_KEY = "!@#_setsuna"
    private val okHttpClientMap = mutableMapOf<String, () -> OkHttpClient>()

    fun getConverter() = converter

    /**
     * 配置全局配置信息
     */
    fun setup(scope: SetsunaBuildScope) {
        baseUrl = scope.baseUrlLazy()
        converter = scope.responseBodyConverterLazy()
        setClient(SETSUNA_CLIENT_KEY, scope.okHttpClientLazy)
    }

    /**
     * 通过 [key] 获取 [OkHttpClient] 实例，没有对应示例时会抛出 [IllegalArgumentException] 异常
     *
     * @see [setClient]
     *
     * @param key 存放 [OkHttpClient] 实例的 key
     * @return 返回 [OkHttpClient] 实例
     */
    fun getClientOrDefault(key: String = SETSUNA_CLIENT_KEY): OkHttpClient {
        return okHttpClientMap[key]?.invoke()
            ?: defaultOkHttpClient
    }

    /**
     * 默认okHttpClient，超时时间为30s
     */
    internal val defaultOkHttpClient = OkHttpClient.Builder()
        .connectTimeout(DEFAULT_TIME_OUT, TimeUnit.SECONDS)
        .writeTimeout(DEFAULT_TIME_OUT, TimeUnit.SECONDS)
        .readTimeout(DEFAULT_TIME_OUT, TimeUnit.SECONDS)
        .build()

    /**
     * 通过 [key] 缓存 [OkHttpClient] 实例
     *
     * @see [getClientOrDefault]
     *
     * @param key key
     * @param client [OkHttpClient] 实例
     */
    fun setClient(key: String, client: () -> OkHttpClient) {
        okHttpClientMap[key] = client
    }

}

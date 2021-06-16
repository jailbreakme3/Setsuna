package com.jailbreakme3.setsuna.setsuna

import com.jailbreakme3.setsuna.RequestType
import org.funktionale.partials.partially1

/**
 * 网络请求类
 */
object Setsuna {

    // 目标是传入context的同时，做好setUp的动作(用kolley的形式
    fun init(build: SetsunaBuildScope.() -> Unit = {}) {
        SetsunaContext.setup(SetsunaBuildScope().apply(build))
        // client
        // baseUrl
        // converter
        // handleTokenError
    }

    private val request: (RequestType, RequestWrapper.() -> Unit) -> Unit =
        { method, init ->
            val baseRequest = RequestWrapper()
            baseRequest.method = method
            // 入参RequestWrapper的实例化
            baseRequest.init() // 执行闭包，完成数据填充
            baseRequest.execute() // 添加到执行队列，自动执行
        }


    // 作为扩展属性
    val get = request.partially1(RequestType.GET)

    // partially1 方法，是传入 request 的 RequestType ，
    // upload也使用的是post
    val post = request.partially1(RequestType.POST)
    val put = request.partially1(RequestType.PUT)
    val delete = request.partially1(RequestType.DELETE)
}

//    getOkHttpInterceptors().forEach { builder.addInterceptor(it) }
//    getGlobalOkHttpNetworkInterceptors("").forEach { builder.addNetworkInterceptor(it) }

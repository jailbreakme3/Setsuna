package com.jailbreakme3.setsuna

import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response

/**
 * 创建一下新的 HttpUrl 对象
 *
 * @param block
 */
internal fun HttpUrl.newHttpUrl(url: String? = null, block: (HttpUrl.Builder.() -> Unit)? = null): HttpUrl {
    val builder = if (url == null) {
        newBuilder()
    } else {
        newBuilder(url) ?: newBuilder()
    }
    return builder.apply {
        block?.invoke(this)
    }.build()
}


/**
 * 执行网络请求
 *
 * @param request 网络请求信息
 */
internal fun OkHttpClient.call(request: Request): Response {
    return newCall(request).execute()
}

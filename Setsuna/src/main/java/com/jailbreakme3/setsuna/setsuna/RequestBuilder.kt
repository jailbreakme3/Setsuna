package com.jailbreakme3.setsuna.setsuna

import com.jailbreakme3.setsuna.RequestType
import com.jailbreakme3.setsuna.log
import com.jailbreakme3.setsuna.newHttpUrl
import okhttp3.Headers
import okhttp3.HttpUrl
import okhttp3.Request
import okhttp3.RequestBody


/**
 * OkHttp的Request对象构建
 *
 * @receiver [HttpKit]
 * @param params Map<String, String>?
 * @param url String
 * @param body RequestBody?
 * @param headers Map<String, String>?
 * @param requestType RequestType?
 * @return Request
 */
fun buildRequestFull(
    params: Map<String, String> = mapOf(),
    url: String,
    body: RequestBody? = null,
    headers: Map<String, String> = mapOf(),
    requestType: RequestType? = null
): Request {
    return buildRequest {
        (takeIf { requestType == RequestType.GET }?.let {
            getGetUrl(
                url,
                params
            ) { it.toQueryString() }
        } ?: url)
            .also {
                buildUrl(it).newHttpUrl {
                    for ((key, value) in params) {
                        addQueryParameter(key, value)
                    }
                }.apply {
                    url(this)
                    this.log()
                }
            }
        when (requestType) {
            RequestType.POST -> post(body!!)
            RequestType.PUT -> put(body!!)
            RequestType.DELETE -> delete(body!!)
            else -> get()
        }
        if (!headers.isNullOrEmpty()) {
            headers(Headers.of(headers.toMutableMap()))
            headers.log()
        }
    }
}


/**
 * 构建 OkHttp网络库的 [okhttp3.Request] 对象
 *
 * @param block [okhttp3.Request.Builder] 接受者作用域
 * @return 返回 OkHttp 中的 [okhttp3.Request] 对象
 *
 * @see [okhttp3.Request]
 */
fun buildRequest(block: (Request.Builder.() -> Unit)? = null): Request {
    return Request.Builder().apply {
        block?.invoke(this)
    }.build()
}

private fun getGetUrl(
    url: String,
    params: Map<String, String>,
    toQueryString: (map: Map<String, String>) -> String
): String {
    return if (params.isEmpty()) url else "$url?${toQueryString(params)}"
}

private fun <K, V> Map<K, V>.toQueryString(): String =
    this.map { "${it.key}=${it.value}" }.joinToString("&")

/**
 * 判断是否有http与https前 缀判断，忽略大小写，有则不加baseUrl，否则加baseUrl
 *
 * @receiver [HttpKit]
 * @param url String
 * @return HttpUrl
 */
private fun buildUrl(url: String): HttpUrl =
    if (url.startsWith("http://", true) || url.startsWith("https://", true)) {
        HttpUrl.get(url)
    } else {
        HttpUrl.get(SetsunaContext.baseUrl + url)
    }
package com.jailbreakme3.setsuna.setsuna

import com.jailbreakme3.setsuna.RequestType
import com.jailbreakme3.setsuna.call
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Request
import okhttp3.Response
import org.funktionale.partials.partially1
import java.io.IOException
import java.util.*
import kotlin.coroutines.resumeWithException


/**
 * 用于请求DSL的构建
 */
class RequestScope {
    var url = ""
    internal val _params: MutableMap<String, String> =
        mutableMapOf()
    internal val _header: MutableMap<String, String> = mutableMapOf()

    private val pairs =
        fun(map: MutableMap<String, String>, makePairs: RequestPairs.() -> Unit) {
            val requestPair = RequestPairs()
            requestPair.makePairs()
            map.putAll(requestPair.pairs)
        }

    /**
     * 提供3中param配置方式
     */
    val param = pairs.partially1(_params)

    fun param(map: Map<String, String>) {
        _params.putAll(map)
    }

    fun param(vararg pairs: Pair<String, String>) {
        pairs.forEach {
            _params[it.first] = it.second
        }
    }

    /**
     * 提供3种header配置方式
     */
    val header = pairs.partially1(_header)

    fun header(map: Map<String, String>) {
        _header.putAll(map)
    }

    fun header(vararg pairs: Pair<String, String>) {
        pairs.forEach {
            _header[it.first] = it.second
        }
    }


    class RequestPairs {
        val pairs: MutableMap<String, String> = HashMap()
        operator fun String.minus(value: String) {
            pairs[this] = value
        }
    }

    operator fun invoke(type: RequestType = RequestType.GET): Request {
        return buildRequestFull(requestType = type, params = _params, url = url, headers = _header)
    }

}

/**
 * Get Post Put Delete 需自行定义运行线程/协程
 */
fun get(requestScope: RequestScope.() -> Unit): Response =
    SetsunaContext.getClientOrDefault().call(
        RequestScope().apply(requestScope).invoke(RequestType.GET)
    )

fun post(requestScope: RequestScope.() -> Unit): Response =
    SetsunaContext.getClientOrDefault().call(
        RequestScope().apply(requestScope).invoke(RequestType.POST)
    )

fun put(requestScope: RequestScope.() -> Unit): Response =
    SetsunaContext.getClientOrDefault().call(
        RequestScope().apply(requestScope).invoke(RequestType.PUT)
    )


fun delete(requestScope: RequestScope.() -> Unit): Response =
    SetsunaContext.getClientOrDefault().call(
        RequestScope().apply(requestScope).invoke(RequestType.DELETE)
    )


/**
 * 在协程中执行请求
 */
@ExperimentalCoroutinesApi
suspend fun awaitGet(requestScope: RequestScope.() -> Unit): Response =
    enqueueCall(
        SetsunaContext.getClientOrDefault()
            .newCall(RequestScope().apply(requestScope).invoke(RequestType.GET))
    )

@ExperimentalCoroutinesApi
suspend fun awaitPost(requestScope: RequestScope.() -> Unit): Response =
    enqueueCall(
        SetsunaContext.getClientOrDefault()
            .newCall(RequestScope().apply(requestScope).invoke(RequestType.PUT))
    )

@ExperimentalCoroutinesApi
suspend fun awaitPut(requestScope: RequestScope.() -> Unit): Response =
    enqueueCall(
        SetsunaContext.getClientOrDefault()
            .newCall(RequestScope().apply(requestScope).invoke(RequestType.PUT))
    )

@ExperimentalCoroutinesApi
suspend fun awaitDelete(requestScope: RequestScope.() -> Unit): Response =
    enqueueCall(
        SetsunaContext.getClientOrDefault()
            .newCall(RequestScope().apply(requestScope).invoke(RequestType.DELETE))
    )


@ExperimentalCoroutinesApi
private suspend fun enqueueCall(call: Call): Response {
    return suspendCancellableCoroutine { continuation ->
        continuation.invokeOnCancellation {
            call.cancel()
        }
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                continuation.resumeWithException(e)
            }

            override fun onResponse(call: Call, response: Response) {
                continuation.resume(response) {

                }
            }
        })
    }
}
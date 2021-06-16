package com.jailbreakme3.setsuna


import okhttp3.Response

/**
 * Http异常类。在网络接口失败的时候进行反序列化操作时，会抛出此异常。
 *
 * http status code在 [200,300) 区间内，表示网络接口请求成功。
 */
class HttpError(private val response: Response) : Exception("HTTP " + response.code() + " " + response.message()) {

    private val code: Int = response.code()
    private val _message: String = response.message()

    /**
     * HTTP status code.
     */
    fun code(): Int {
        return code
    }

    /**
     * HTTP status message.
     */
    fun message(): String {
        return _message
    }

    /**
     * The full HTTP response. This may be null if the exception was serialized.
     */
    fun response(): Response {
        return response
    }
}
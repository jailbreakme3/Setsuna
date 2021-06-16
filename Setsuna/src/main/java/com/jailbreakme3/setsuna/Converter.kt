package com.jailbreakme3.setsuna

import com.google.gson.reflect.TypeToken
import com.jailbreakme3.setsuna.setsuna.SetsunaContext
import okhttp3.Response
import okhttp3.ResponseBody
import okio.Buffer
import java.lang.reflect.Type
import java.nio.charset.Charset

/**
 * 网络请求成功时，对 Response 做反序列操作
 *
 * @receiver Response
 * @return T?
 * 1、Gson反序列化失败返回null
 * 2、!isSuccessful时返回null
 * 3、token失效及其相关处理返回null
 */
inline fun <reified T> Response.parse(): T? {
    return runCatching {
        if (isSuccessful) {
            return SetsunaContext.getConverter().convert(body()!!, genericType<T>())
        } else {
            null
        }
    }.getOrNull()
}

/**
 *
 * 对 Response 做反序列操作
 *
 * @receiver Response
 * @return T?
 * 1、T:正常数据实体返回
 * 3、网络请求失败的情况下会抛出 [HttpError] 错误
 */
inline fun <reified T> Response.parseOrThrow(): T {
    if (isSuccessful) {
        return SetsunaContext.getConverter().convert(body()!!, genericType<T>())
    } else {
        throw HttpError(this)
    }
}

/**
 * 取OkHttp的Response中body转为字符串
 *
 * @return String 解析失败会抛出异常
 */
fun Response.toJsonStr(): String {
    val responseBody = newBuilder().build().body()
    val source = responseBody!!.source()
    source.request(Long.MAX_VALUE)
    val buffer = source.buffer()
    val encoding = headers()["Content-Encoding"]
    val clone = buffer.clone()
    return responseBody.parseContent(encoding, clone)!!
}

internal fun ResponseBody.parseContent(encoding: String?, clone: Buffer): String? {
    var charset = Charset.forName("UTF-8")
    val contentType = contentType()
    if (contentType != null) {
        charset = contentType.charset(charset)!!
    }
    return if (encoding != null && encoding.equals("gzip", true)) {
        clone.readByteArray().decompressForGzip(convertCharset(charset))
    } else if (encoding != null && encoding.equals("zlib", true)) {
        clone.readByteArray().decompressToStringForZlib(convertCharset(charset))
    } else {
        // content没有被压缩
        clone.readString(charset)
    }
}

private fun convertCharset(charset: Charset): String? {
    val s = charset.toString()
    val i = s.indexOf("[")
    return if (i == -1) s else s.substring(i + 1, s.length - 1)
}


/**
 * 通过 Kotlin 的 reified 获取泛型的真实类型
 */
inline fun <reified T> genericType(): Type = object : TypeToken<T>() {}.type
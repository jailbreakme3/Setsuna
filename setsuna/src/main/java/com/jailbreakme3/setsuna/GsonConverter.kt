package com.jailbreakme3.setsuna


import com.google.gson.Gson
import com.google.gson.JsonIOException
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonToken
import okhttp3.ResponseBody
import java.lang.reflect.Type

/**
 * 基于 Gson 实现的反序列化类，实现了 [ResponseBodyConverter] 接口
 *
 * @param gson [Gson] 对象
 */
class GsonConverter(private val gson: Gson) : ResponseBodyConverter {

    @Suppress("UNCHECKED_CAST")
    override fun <T> convert(response: ResponseBody, type: Type): T {
        val adapter = gson.getAdapter(TypeToken.get(type))
        val jsonReader = gson.newJsonReader(response.charStream())
        return response.use {
            adapter.read(jsonReader).also {
                if (jsonReader.peek() != JsonToken.END_DOCUMENT) {
                    throw JsonIOException("JSON document was not fully consumed.")
                }
            } as T
        }
    }

    companion object {

        /**
         * 工厂方法，用来实例化 [GsonConverter] 对象

         * @param gson [Gson] 对象，默认创建一个新的 [Gson] 对象
         */
        @JvmStatic
        fun create(gson: Gson = Gson()): GsonConverter {
            return GsonConverter(gson)
        }

    }
}
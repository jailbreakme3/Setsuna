package com.jailbreakme3.setsuna.setsuna

import com.jailbreakme3.setsuna.HttpError
import com.jailbreakme3.setsuna.RequestType
import com.jailbreakme3.setsuna.call
import com.jailbreakme3.setsuna.parse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.RequestBody
import okhttp3.Response
import org.funktionale.partials.partially1

class RequestWrapper {

    internal var method: RequestType = RequestType.GET
    var url: String = ""
    private var body: RequestBody? = null
    private var _start: (() -> Unit) = {}
    private var _success: (Response) -> Unit = {}
    private var _fail: (Throwable) -> Unit = {}
    private var _finish: (() -> Unit) = {}

    // queryParameter
    private val _params: MutableMap<String, String> =
        mutableMapOf()
    private val _fileParams: MutableMap<String, String> =
        mutableMapOf()
    private val _headers: MutableMap<String, String> = mutableMapOf()


    /**
     * execute the call
     * attention please: the callback is all running on main thread,
     * thus do the asynchronous job and etc on your own within the implementation
     */
    internal fun execute() {
        // 默认在组件内的scope中请求
        SetsunaCoroutineScope.launch(Dispatchers.IO) {
            val request =
                buildRequestFull(
                    params = _params,
                    url = url,
                    headers = _headers,
                    body = body,
                    requestType = method
                )
            withContext(Dispatchers.Main) {
                _start()
            }
            kotlin.runCatching {
                // 执行网络请求，并回传请求结果
                SetsunaContext.getClientOrDefault().call(request)
            }.onSuccess {
                // will there be a success with response code 4xx ?
                if (it.isSuccessful) {
                    withContext(Dispatchers.Main) {
                        _success(it)
                    }
                } else {
                    _fail(HttpError(it))
                }
            }.onFailure {
                withContext(Dispatchers.Main) {
                    _fail(it)
                }
            }
            withContext(Dispatchers.Main) {
                _finish()
            }
        }
    }

    fun onStart(onStart: () -> Unit) {
        _start = onStart
    }

    fun onFail(onError: (Throwable) -> Unit) {
        _fail = onError
    }

    /**
     * 成功回调
     */
    fun onSuccess(onSuccess: (Response) -> Unit) {
        _success = onSuccess
    }

    /**
     * 成功并解析
     */
    inline fun <reified T> onSuccessWithParse(crossinline success: (T?) -> Unit) {
        onSuccess {
            success(it.parse<T>())
        }
    }

    /**
     * 请求结束
     */
    fun onFinish(onFinish: () -> Unit) {
        _finish = onFinish
    }

    private val pairs =
        fun(map: MutableMap<String, String>, makePairs: RequestScope.RequestPairs.() -> Unit) {
            val requestPair = RequestScope.RequestPairs()
            requestPair.makePairs()
            map.putAll(requestPair.pairs)
        }

    val params = pairs.partially1(_params)
    val headers = pairs.partially1(_headers)
    val files = pairs.partially1(_fileParams)

}


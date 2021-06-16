package com.jailbreakme3.setsuna

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jailbreakme3.setsuna.databinding.ActivityMainBinding
import com.jailbreakme3.setsuna.setsuna.Setsuna
import com.jailbreakme3.setsuna.setsuna.SetsunaCoroutineScope
import com.jailbreakme3.setsuna.setsuna.get
import kotlinx.coroutines.*
import okhttp3.OkHttpClient

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    companion object {
        // open api
        private val APP_ID = "app_id" to "xxxxxxx"
        private val APP_SECRET = "app_secret" to "xxxxxxxx"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // init Setsuna
        Setsuna.init {
            // optional
            baseUrl("https://www.baidu.com/")
            // customized okHttpClient(optional, when empty, use default client)
            okHttpClient {
                OkHttpClient()
            }
            converter {
                // conventer which implements ResponseBodyConverter (optional)
                GsonConverter.create()
            }
        }

        request()
    }

    // 测试
    private fun request() {
        SetsunaCoroutineScope.launch {
            get {
                // https://github.com/MZCretin/RollToolsApi
                url = "/qrcode/create/single"
                param {
                    "content" - "测试内容"
                    "type" - "0"
                }
                param(mapOf("key" to "value"))
                param("key" to "value")
                // optional grammar
                header {
                    "key" - "xxxxx"
                }
                header(APP_ID, APP_SECRET)
            }.parseOrThrow<QRCodeData>().apply {
                this.log()
            }

            val result = get {
                url = ""
            }.parse<QRCodeData>()

            // run in a new thread or corountine
            Setsuna.get {
                url = ""
                params {
                    "content" - "测试内容"
                    "type" - "0"
                }
                headers {
                    "key" - "value"
                }

                onSuccess {
                    // callback when request succeed
                    // handle the response yourself
                    it.body()
                }

                onSuccessWithParse<QRCodeData> {
                    // callback when request succeed
                    // get the response with a parsed nullable entity
                }

                // optional below
                onStart {
                    // callback when request start
                }

                onFinish {
                    // callback when request finished
                }

                onFail {
                    // callback when request failed
                }
            }

        }
    }

}
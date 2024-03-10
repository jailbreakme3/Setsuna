package com.jailbreakme3.setsuna

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jailbreakme3.setsuna.databinding.ActivityMainBinding
import com.jailbreakme3.setsuna.setsuna.Setsuna
import com.jailbreakme3.setsuna.setsuna.SetsunaCoroutineScope
import com.jailbreakme3.setsuna.setsuna.get
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

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
//            baseUrl("")
            converter {
                GsonConverter.create()
            }
            okHttpClient {
                OkHttpClient.Builder()
                    .addInterceptor(
                        // 拦截器
                        HttpLoggingInterceptor().setLevel(
                            HttpLoggingInterceptor.Level.BODY
                        )
                    )
                    .addInterceptor {
                        it.proceed(
                            it.request()
                                .newBuilder()
                                // 全局 header
                                .header("token", "xxxxxx")
                                .addHeader("version", "3.0.0")
                                .build()
                        )
                    }
                    .build()
            }

        }

        binding.btStart.setOnClickListener {
            request()
        }

    }


    // 测试
    private fun request() {
        SetsunaCoroutineScope.launch {
            Setsuna.post {
                url = "https://${binding.etUrl.text}"
                params {
                    "content" - "测试内容"
                    "type" - "0"
                }
                headers {
                    "key" - "value"
                }
                onSuccessWithParse<QRCodeData> {
                    val result = it ?: QRCodeData()
                    // 拿到返回值
                    binding.tvResponse.text = result.toString()
                }
                onFail {
                    binding.tvResponse.text = it.message?:"请求错误"
                }
            }

        }
    }

}
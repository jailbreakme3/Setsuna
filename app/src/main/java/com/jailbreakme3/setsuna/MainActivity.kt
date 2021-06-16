package com.jailbreakme3.setsuna

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jailbreakme3.setsuna.databinding.ActivityMainBinding
import com.jailbreakme3.setsuna.setsuna.Setsuna
import com.jailbreakme3.setsuna.setsuna.SetsunaCoroutineScope
import com.jailbreakme3.setsuna.setsuna.get
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    companion object {
        // open api
        private val APP_ID = "app_id" to "pqlqfhmustlmrrek"
        private val APP_SECRET = "app_secret" to "cU56aTl1WS9rV3FrQjRzMFE5MTZkUT09"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // init Setsuna
        Setsuna.init {
            baseUrl("https://www.mxnzp.com/api")
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
                // optional grammar
//                header {
//                    "app_id" - "pqlqfhmustlmrrek"
//                    "app_secret" - "cU56aTl1WS9rV3FrQjRzMFE5MTZkUT09"
//                }
                header(APP_ID, APP_SECRET)
            }.parseOrThrow<QRCodeData>().apply {
                this.log()
            }

        }
    }

}
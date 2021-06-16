package com.jailbreakme3.setsuna

import android.os.Build
import com.jailbreakme3.setsuna.setsuna.Setsuna
import com.jailbreakme3.setsuna.setsuna.SetsunaCoroutineScope
import com.jailbreakme3.setsuna.setsuna.get
import kotlinx.coroutines.launch
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@Config(sdk = [Build.VERSION_CODES.P], manifest = Config.NONE)
@RunWith(RobolectricTestRunner::class)
class SetsunaTest {

    companion object {
        private val APP_ID = "app_id" to "pqlqfhmustlmrrek"
        private val APP_SECRET = "app_secret" to "cU56aTl1WS9rV3FrQjRzMFE5MTZkUT09"
    }

    @Before
    fun init() {
        Setsuna.init {
            baseUrl("https://www.mxnzp.com/api")
        }
    }

    @Test
    fun test() {
        SetsunaCoroutineScope.launch {

            val a = get {
                // https://github.com/MZCretin/RollToolsApi
                url = "/qrcode/create/single"
                param {
                    "content" - "测试内容"
                    "type" - "0"
                }
                header {
                    APP_ID
                    APP_SECRET
                }
            }.parse<QRCodeData>()
            a?.data?.qrCodeUrl.print()
        }
    }

    private fun Any?.print() {
        print(this)
    }

}
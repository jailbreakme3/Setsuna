package com.jailbreakme3.setsuna

import androidx.annotation.Keep

@Keep
data class QRCodeData(
    val code: String = "",
    val msg: String = "",
    val data: Data = Data()
) {
    data class Data(
        val qrCodeUrl: String = "",
        val content: String = "",
        val type: Int = 0,
        val qrCodeBase64: String = "",
    )
}
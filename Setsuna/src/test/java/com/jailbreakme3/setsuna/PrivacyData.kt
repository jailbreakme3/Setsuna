package com.jailbreakme3.setsuna


import com.google.gson.annotations.SerializedName

data class PrivacyData(
    @SerializedName("code")
    val code: Int = 0,
    @SerializedName("msg")
    val msg: String = "",
    @SerializedName("result")
    val result: Result = Result()
) {
    data class Result(
        @SerializedName("news_id")
        val newsId: Int = 0,
        @SerializedName("title")
        val title: String = "",
        @SerializedName("url")
        val url: String = ""
    )
}
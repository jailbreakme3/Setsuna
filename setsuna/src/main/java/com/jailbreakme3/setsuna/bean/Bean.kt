package com.jailbreakme3.setsuna.bean

import com.google.gson.annotations.SerializedName

/**
 * Token相关通用的bean
 * {
 *   "code": 0,
 *   "message": "invalid token",
 *   "data": {
 *      "code": "0108",
 *      "mutime_long": "1587631789130",
 *      "mutimelong": "1587631789130",
 *      "mutime": "2020-04-23 08:49"
 *      }
 *  }
 */

data class CommonTokenBean(
    val code: Int,
    val data: Data,
    val message: String
)

data class Data(
    val code: String,
    val mutime: String,
    @SerializedName("mutime_long")
    val mutimeLong: String,
    val mutimelong: String
)
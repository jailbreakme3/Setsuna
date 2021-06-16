package com.jailbreakme3.setsuna

import android.util.Log
import okhttp3.Request
import java.util.*

private const val TAG = "Setsuna"

fun Any?.log() {
    Log.i(TAG, "$this")
}

fun Any?.log(tag: String) {
    Log.i(TAG, "$tag: $this")
}
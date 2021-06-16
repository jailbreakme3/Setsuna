package com.jailbreakme3.setsuna.listener


class DownLoadListener {
    internal var started: (() -> Unit)? = null
    internal var progress: ((Int) -> Unit)? = null
    internal var failed: ((Throwable) -> Unit)? = null
    internal var completed: ((String?) -> Unit)? = null

    @Suppress("unused")
    fun onStarted(block: () -> Unit) {
        started = block
    }

    @Suppress("unused")
    fun onProgress(block: (Int) -> Unit) {
        progress = block
    }

    @Suppress("unused")
    fun onFailed(block: (Throwable) -> Unit) {
        failed = block
    }

    @Suppress("unused")
    fun onCompleted(block: ((String?) -> Unit)?) {
        completed = block
    }
}
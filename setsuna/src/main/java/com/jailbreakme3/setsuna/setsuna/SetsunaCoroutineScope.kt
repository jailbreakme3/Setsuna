package com.jailbreakme3.setsuna.setsuna

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlin.coroutines.CoroutineContext

/**
 * Setsuna customized coroutine scope
 */
object SetsunaCoroutineScope : CoroutineScope {

    private val context by lazy { SupervisorJob() + Dispatchers.IO }

    override val coroutineContext: CoroutineContext
        get() = context
}
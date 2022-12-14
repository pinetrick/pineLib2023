package com.pine.lib.addone

import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext


/**
 * Timer function
 * How to Use
 * ArtooTimer()
.setInterval(500)
.setCallbackThread(Dispatchers.Main)
.setOnTimerListener {
//DO something
}
.start(5) // Repeat 5 times

ArtooTimer()
.setOnTimerListener {
//DO something
}
.start() // Loop until call stop()
 */
class MyTimer {

    private var onTimerListener: (suspend (MyTimer) -> Unit)? = null
    private var cancelableJob: Job? = null
    private var callbackThread: CoroutineContext = Dispatchers.Main
    private var interval: Long = 1000

    fun stop(): MyTimer {
        cancelableJob?.let {
            it.cancel()
            cancelableJob = null
        }

        return this
    }

    fun start(looperTimes: Int = -1): MyTimer {
        stop()

        cancelableJob = CoroutineScope(Dispatchers.Default).launch {
            var leftTimes: Int = looperTimes
            while (leftTimes == -1 || leftTimes-- > 0) {
                delay(interval)
                onTimer()
            }
        }

        return this
    }

    fun setCallbackThread(callbackThread: CoroutineContext = Dispatchers.Main): MyTimer {
        this.callbackThread = callbackThread
        return this
    }

    fun setOnTimerListener(callback: suspend (MyTimer) -> Unit): MyTimer {
        this.onTimerListener = callback
        return this
    }


    fun setInterval(interval: Long): MyTimer {
        if (interval > 0 && this.interval != interval) {
            this.interval = interval

            cancelableJob?.let {
                start()
            }
        }
        return this
    }

    private suspend fun onTimer() = withContext(callbackThread) {
        onTimerListener?.invoke(this@MyTimer)
    }
}
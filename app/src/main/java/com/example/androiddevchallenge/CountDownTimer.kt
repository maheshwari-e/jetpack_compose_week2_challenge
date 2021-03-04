package com.example.androiddevchallenge

import android.os.CountDownTimer

class CountDownTimer(millisSec: Long, private val countDownInterval: Long ) {
    private var listener: (CountDownTimerEvent) -> Unit = {}
    private var countDownTimer: CountDownTimer? = null
    private var currentMillisSeconds = millisSec

    fun setCountDownTimerEventListener(listener: (CountDownTimerEvent) -> Unit) {
        this.listener = listener
    }

    fun startTimer(){
        countDownTimer = object: CountDownTimer(currentMillisSeconds, countDownInterval){
            override fun onTick(millisUntilFinished: Long) {
                currentMillisSeconds = millisUntilFinished
                listener(CountDownTimerEvent.TickEvent(millisUntilFinished))
            }

            override fun onFinish() {
                listener(CountDownTimerEvent.FinishEvent)
            }
        }
        countDownTimer?.start()
    }

    fun stopTimer() {  countDownTimer?.cancel() }

}

sealed class CountDownTimerEvent {
    data class TickEvent(val millisSeconds: Long): CountDownTimerEvent()
    object FinishEvent: CountDownTimerEvent()
}

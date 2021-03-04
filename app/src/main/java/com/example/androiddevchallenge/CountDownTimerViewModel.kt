package com.example.androiddevchallenge

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlin.math.ceil

class CountDownTimerViewModel : ViewModel() {
    private val _hours = MutableLiveData("00")
    private val _minutes = MutableLiveData("00")
    private val _seconds = MutableLiveData("30")

    private val _isTimerRunning = MutableLiveData(false)
    val isTimerRunning: LiveData<Boolean> = _isTimerRunning

    val hours: LiveData<String> = _hours
    val minutes: LiveData<String> = _minutes
    val seconds: LiveData<String> = _seconds

    private val _tickTime = MutableLiveData("00:00:00")
    val tickTime: LiveData<String> = _tickTime

    private val _second = MutableLiveData<Long>(0)
    val second: LiveData<Long> = _second

    var maxSecond = 0L

    fun onTimeHoursChanged(time: String) {
            _hours.value = if (time.length > 2) _hours.value else time
    }
    fun onTimeMintesChanged(time: String) {
        _minutes.value =  if (time.length > 2) _minutes.value else time
    }
    fun onTimeSecondsChanged(time: String) {
        _seconds.value =  if (time.length > 2) _seconds.value else time
    }

    private lateinit var countDownTimer: CountDownTimer
    private fun createWorkoutTimer(countdownTimer: Long) =
        CountDownTimer(countdownTimer, 1000L)
        .apply { setCountDownTimerEventListener{ event: CountDownTimerEvent ->
            when(event){
                is CountDownTimerEvent.TickEvent -> {
                    val second = ceil(event.millisSeconds.toDouble() / 1000).toLong()
                    _second.value = second
                    _tickTime.value = String.format("%02d:%02d:%02d",
                        (second/3600),
                        ((second % 3600)/60),
                        (second % 60)) }
                is CountDownTimerEvent.FinishEvent -> {
                    _isTimerRunning.value = false
                }
            }
        }
        }

    fun onStartClick(){
        val countdown = (_hours.value!!.toLong() * 3600 +
                _minutes.value!!.toLong() * 60 +
                _seconds.value!!.toLong()) * 1000L
        maxSecond = countdown / 1000
        countDownTimer = createWorkoutTimer(countdown)
        countDownTimer.startTimer()
        _isTimerRunning.value = true
    }

    fun onCancelClick(){
        countDownTimer.stopTimer()
        _isTimerRunning.value = false
    }

    override fun onCleared() {
        super.onCleared()
        countDownTimer.stopTimer()
        _second.value = 0
    }
}
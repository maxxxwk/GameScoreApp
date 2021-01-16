package com.maxxxwk.gamescoreapp.utils

import android.os.CountDownTimer
import android.widget.TextView
import com.maxxxwk.gamescoreapp.callbacks.TimerCallback

class GameTimer(
    private var minutes: Int,
    private var seconds: Int,
    private val timerView: TextView,
    private val callback: TimerCallback
) {
    private lateinit var countDownTimer: CountDownTimer

    init {
        updateTimerState()
    }

    fun startTimer() {
        setupCountDownTimer()
        if (minutes != 0 || seconds != 0) {
            countDownTimer.start()
        }
    }

    fun stopTimer() {
        countDownTimer.cancel()
    }

    private fun updateTimerState() {
        timerView.text = when {
            minutes < 10 && seconds < 10 -> "0$minutes:0$seconds"
            minutes >= 10 && seconds < 10 -> "$minutes:0$seconds"
            minutes < 10 && seconds >= 10 -> "0$minutes:$seconds"
            else -> "$minutes:$seconds"
        }
    }

    private fun setupCountDownTimer() {
        val timeInMilliseconds = (minutes * 60 + seconds) * 1000L
        countDownTimer = object : CountDownTimer(timeInMilliseconds, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                if (seconds > 0) {
                    seconds -= 1
                } else {
                    seconds = 59
                    minutes -= 1
                }
                updateTimerState()
            }

            override fun onFinish() {
                callback.onFinish()
            }
        }
    }
}
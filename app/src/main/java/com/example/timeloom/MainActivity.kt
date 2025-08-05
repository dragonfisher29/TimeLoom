package com.example.timeloom

import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var timerText: TextView
    private lateinit var startButton: Button
    private lateinit var resetButton: Button
    private lateinit var pauseButton: Button

    private var countDownTimer: CountDownTimer? = null
    private var timeLeftInMillis: Long = 25 * 60 * 1000 // 25 minutes
    private var isTimerRunning = false

    override fun onCreate(savedInstanceState: Bundle?) {
        supportActionBar?.hide()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        timerText = findViewById(R.id.timer_text)
        startButton = findViewById(R.id.start_button)
        resetButton = findViewById(R.id.reset_button)
        pauseButton= findViewById(R.id.pauseButton)

        updateTimerText()

        startButton.setOnClickListener {
            if (!isTimerRunning) {
                startTimer()
            }
        }

        resetButton.setOnClickListener {
            resetTimer()
        }

        pauseButton.setOnClickListener {
            pauseTimer()
        }
    }

    private fun pauseTimer() {
        if (isTimerRunning) {
            countDownTimer?.cancel()
            isTimerRunning = false
        }
    }

    private fun startTimer() {
        countDownTimer = object : CountDownTimer(timeLeftInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeftInMillis = millisUntilFinished
                updateTimerText()
            }

            override fun onFinish() {
                isTimerRunning = false
                timerText.text = getString(R.string.done)
            }
        }.start()

        isTimerRunning = true
    }

    private fun resetTimer() {
        countDownTimer?.cancel()
        timeLeftInMillis = 25 * 60 * 1000
        updateTimerText()
        isTimerRunning = false
    }

    private fun updateTimerText() {
        val minutes = (timeLeftInMillis / 1000) / 60
        val seconds = (timeLeftInMillis / 1000) % 60
        val timeFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
        timerText.text = timeFormatted
    }
}

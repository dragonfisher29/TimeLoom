package com.example.timeloom

import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var timerText: TextView
    private lateinit var startButton: Button
    private lateinit var resetButton: Button
    private lateinit var pauseButton: Button
    private lateinit var sessionText: TextView

    private var countDownTimer: CountDownTimer? = null
    private var timeLeftInMillis: Long = 25 * 60 * 1000 // 25 minutes
    private var isTimerRunning = false
    private var isWorkSession = true
    private var isPaused = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        sessionText = findViewById(R.id.session_Text)
        sessionText.text = if (isWorkSession) getString(R.string.work_time) else getString(R.string.break_time)

        timerText = findViewById(R.id.timer_text)
        startButton = findViewById(R.id.start_button)
        resetButton = findViewById(R.id.reset_button)
        pauseButton= findViewById(R.id.pauseButton)

        updateTimerText()

        startButton.setOnClickListener {
            if (!isTimerRunning && !isPaused) {
                startTimer()
            }
        }

        resetButton.setOnClickListener {
            if (isTimerRunning || isPaused) {
                AlertDialog.Builder(this)
                    .setTitle("Reset Timer")
                    .setMessage("Are you sure you want to reset the timer?")
                    .setPositiveButton("Yes") { _, _ ->
                        resetTimer()
                    }
                    .setNegativeButton("No", null)
                    .show()
            } else {
                resetTimer()
            }
        }

        pauseButton.setOnClickListener {
            pauseTimer()
        }
    }



    private fun pauseTimer() {
        if (isTimerRunning) {
            countDownTimer?.cancel()
            isTimerRunning = false
            isPaused = true
            pauseButton.text = getString(R.string.resume)
        } else if (isPaused) {
            startTimer()
            isPaused = false
            pauseButton.text = getString(R.string.pause)
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
                startButton.isEnabled = true
                pauseButton.text = getString(R.string.pause)

                isWorkSession = !isWorkSession
                sessionText.text = if (isWorkSession)
                    getString(R.string.work_time)
                else
                    getString((R.string.break_time))

                timeLeftInMillis = if (isWorkSession)
                    25 * 60 * 1000
                else
                    5 * 60 * 1000

                updateTimerText()
                startTimer()
            }

        }.start()

        isTimerRunning = true
        startButton.isEnabled = false
        pauseButton.text = getString(R.string.pause)
    }

    private fun resetTimer() {
        countDownTimer?.cancel()
        timeLeftInMillis = 25 * 60 * 1000
        updateTimerText()
        isTimerRunning = false
        isPaused = false
        startButton.isEnabled = true
        pauseButton.text  = getString(R.string.pause)
    }

    private fun updateTimerText() {
        val minutes = (timeLeftInMillis / 1000) / 60
        val seconds = (timeLeftInMillis / 1000) % 60
        val timeFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
        timerText.text = timeFormatted
    }


}

package com.example.clockapp

import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    private lateinit var digitalClock: TextView
    private lateinit var countdownTimer: TextView
    private lateinit var startCountdownButton: Button
    private lateinit var pauseCountdownButton: Button
    private lateinit var resetCountdownButton: Button
    private lateinit var countdownInput: EditText
    private val handler = Handler(Looper.getMainLooper())
    private var timer: CountDownTimer? = null
    private var timeLeftInMillis: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 初始化控件
        digitalClock = findViewById(R.id.digitalClock)
        countdownTimer = findViewById(R.id.countdownTimer)
        startCountdownButton = findViewById(R.id.startCountdownButton)
        pauseCountdownButton = findViewById(R.id.pauseCountdownButton)
        resetCountdownButton = findViewById(R.id.resetCountdownButton)
        countdownInput = findViewById(R.id.countdownInput)

        // 更新数字时钟
        updateDigitalClock()

        // 按钮点击事件
        startCountdownButton.setOnClickListener {
            startCountdown()
        }
        pauseCountdownButton.setOnClickListener {
            pauseCountdown()
        }
        resetCountdownButton.setOnClickListener {
            resetCountdown()
        }


    }

    private fun updateDigitalClock() {
        val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault())
        digitalClock.text = sdf.format(Date())
        handler.postDelayed({ updateDigitalClock() }, 1000)
    }

    private fun startCountdown() {
        val inputSeconds = countdownInput.text.toString().toLongOrNull() ?: 50
        timeLeftInMillis = inputSeconds * 1000

        timer?.cancel()
        timer = object : CountDownTimer(timeLeftInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeftInMillis = millisUntilFinished
                countdownTimer.text = (millisUntilFinished / 1000).toString()
            }

            override fun onFinish() {
                countdownTimer.text = "0"
                Toast.makeText(this@MainActivity, "倒计时结束！", Toast.LENGTH_SHORT).show()
            }
        }.start()
    }

    private fun pauseCountdown() {
        timer?.cancel()
        countdownTimer.text = (timeLeftInMillis / 1000).toString()
    }

    private fun resetCountdown() {
        timer?.cancel()
        timeLeftInMillis = 0
        countdownTimer.text = "0"
        countdownInput.setText("")
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
        timer?.cancel()
    }
}
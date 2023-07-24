package com.rohan.pixd.ui.splash

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.rohan.pixd.R
import com.rohan.pixd.ui.main.MainActivity

class SplashActivity : AppCompatActivity() {

    private val SPLASH_DISPLAY_TIME = 2000L // 2 seconds

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Delay for the specified time and then start the main activity
        Thread {
            Thread.sleep(SPLASH_DISPLAY_TIME)
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }.start()
    }
}

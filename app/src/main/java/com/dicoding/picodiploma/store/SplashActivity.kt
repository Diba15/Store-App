package com.dicoding.picodiploma.store

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import kotlin.math.log

class SplashActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    private val PREFS_NAME = "LoginPref"
    private val KEY_LOGED = "key_loged"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
        val loged = sharedPreferences.getBoolean(KEY_LOGED, false)
        if (!loged) {
            Handler().postDelayed({
                kotlin.run {
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            },2500)
        } else {
            Handler().postDelayed({
                kotlin.run {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            },2500)
        }
    }
}
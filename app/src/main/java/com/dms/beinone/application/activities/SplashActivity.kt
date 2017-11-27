package com.dms.beinone.application.activities

import android.content.*
import android.os.*
import android.support.v7.app.*

/**
 * Created by root1 on 2017. 11. 27..
 */
class SplashActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()

    }

}
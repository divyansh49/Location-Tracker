package com.android.locationtracker.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.android.locationtracker.R
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        btnmyloc.setOnClickListener {
            startActivity(Intent(this, MyLocationActivity::class.java))
        }
        btnanotherloc.setOnClickListener {
            startActivity(Intent(this, ViewAnotherLocationActivity::class.java))
        }
    }
}
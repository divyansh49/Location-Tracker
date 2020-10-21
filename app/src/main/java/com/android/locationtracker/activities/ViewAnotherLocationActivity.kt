package com.android.locationtracker.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.android.locationtracker.R
import kotlinx.android.synthetic.main.activity_view_another_location.*

class ViewAnotherLocationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_another_location)

        btnseeinmap.setOnClickListener {
            val elat = etlatitude.text.toString().toDouble()
            val elng = etlongitude.text.toString().toDouble()
            val intent = Intent(this, MapsActivity::class.java)

            intent.putExtra("elat", elat)
            intent.putExtra("elng", elng)
            startActivity(intent)
        }

    }
}
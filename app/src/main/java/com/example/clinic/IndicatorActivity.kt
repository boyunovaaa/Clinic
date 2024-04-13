package com.example.clinic

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class IndicatorActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.indicators)

        findViewById<Button>(R.id.change_data).setOnClickListener {
            val currentUserEmail = intent.getStringExtra("userEmail")
            val intent = Intent(this, EditPersonActivity::class.java)
            intent.putExtra("userEmail", currentUserEmail)
            startActivity(intent)
        }
    }
}

package com.example.clinic

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class PersonalInfoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.personal_info)

        val changeDataButton = findViewById<Button>(R.id.entrance)

        changeDataButton.setOnClickListener {
            val intent = Intent(this, EditPersonActivity::class.java)
            startActivity(intent)
        }

        val goBackButton = findViewById<Button>(R.id.go_back)
        goBackButton.setOnClickListener {
            val intent = Intent(this, IndicatorActivity::class.java)
            startActivity(intent)
        }
    }
}
package com.example.clinic

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.clinic.MainActivity.DBHelper.Companion.COLUMN_EMAIL
import com.example.clinic.MainActivity.DBHelper.Companion.COLUMN_NAME
import com.example.clinic.MainActivity.DBHelper.Companion.COLUMN_PASSWORD
import com.example.clinic.MainActivity.DBHelper.Companion.COLUMN_SURNAME
import com.example.clinic.MainActivity.DBHelper.Companion.TABLE_NAME

class EditPersonActivity : AppCompatActivity() {
    private lateinit var currentUserEmail: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit)

        val emailTextView = findViewById<TextView>(R.id.email)
        val newNameEditText = findViewById<EditText>(R.id.new_name)
        val newSurnameEditText = findViewById<EditText>(R.id.new_surname)
        val newPasswordEditText = findViewById<EditText>(R.id.new_password)
        val saveButton = findViewById<Button>(R.id.save_data)

        currentUserEmail = intent.getStringExtra("userEmail") ?: ""

        emailTextView.text = currentUserEmail

        saveButton.setOnClickListener {
            val newName = newNameEditText.text.toString()
            val newSurname = newSurnameEditText.text.toString()
            val newPassword = newPasswordEditText.text.toString()

            if (newName.isNotEmpty() && newSurname.isNotEmpty() && newPassword.isNotEmpty()) {
                val hashedPassword = PasswordHasher.hashPassword(newPassword)
                updateUserData(currentUserEmail, newName, newSurname, hashedPassword)
            } else {
                Toast.makeText(this, "Пожалуйста, заполните все поля.", Toast.LENGTH_SHORT).show()
            }
        }


    val goBackButton = findViewById<Button>(R.id.go_back)
        goBackButton.setOnClickListener {
            val intent = Intent(this, IndicatorActivity::class.java)
            startActivity(intent)
        }
    }

    private fun updateUserData(email: String, newName: String, newSurname: String, newPassword: String) {
        val dbHelper = MainActivity.DBHelper(this)
        val db = dbHelper.writableDatabase

        val values = ContentValues().apply {
            put(COLUMN_NAME, newName)
            put(COLUMN_SURNAME, newSurname)
            put(COLUMN_PASSWORD, newPassword)
        }

        val selection = "$COLUMN_EMAIL = ?"
        val selectionArgs = arrayOf(email)

        val updatedRows = db.update(TABLE_NAME, values, selection, selectionArgs)

        if (updatedRows > 0) {
            Toast.makeText(this, "Данные пользователя успешно обновлены.", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Ошибка при обновлении данных пользователя.", Toast.LENGTH_SHORT).show()
        }

        db.close()
    }
}

package com.example.clinic

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.clinic.utils.PasswordHasher


class RegistActivity : AppCompatActivity() {

    private lateinit var emailEditText: EditText
    private lateinit var nameEditText: EditText
    private lateinit var surnameEditText: EditText
    private lateinit var numberEditText: EditText
    private lateinit var passwordEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.registration)

        emailEditText = findViewById(R.id.EmailAddress)
        nameEditText = findViewById(R.id.name)
        surnameEditText = findViewById(R.id.surname)
        numberEditText = findViewById(R.id.number)
        passwordEditText = findViewById(R.id.password)

        findViewById<Button>(R.id.entrance).setOnClickListener {
            saveUser()
            val intent = Intent(this, IndicatorActivity::class.java)
            startActivity(intent)
        }

    }

    private fun saveUser() {
        val email = emailEditText.text.toString()
        val name = nameEditText.text.toString()
        val surname = surnameEditText.text.toString()
        val number = numberEditText.text.toString()
        val password = passwordEditText.text.toString()

        if (email.isEmpty() || name.isEmpty() || surname.isEmpty() || number.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show()
            return
        }

        val hashedPassword = PasswordHasher.hashPassword(password)

        val dbHelper = DBHelper(this)
        dbHelper.insertUser(email, name, surname, number, hashedPassword)
    }

    private class DBHelper(context: Context) :
        SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

        override fun onCreate(db: SQLiteDatabase) {
            val createTableQuery = "CREATE TABLE $TABLE_NAME (" +
                    "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "$COLUMN_EMAIL TEXT, " +
                    "$COLUMN_NAME TEXT, " +
                    "$COLUMN_SURNAME TEXT, " +
                    "$COLUMN_NUMBER TEXT, " +
                    "$COLUMN_PASSWORD TEXT)"

            db.execSQL(createTableQuery)
        }

        override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
//            db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
            onCreate(db)
        }

        fun insertUser(email: String, name: String, surname: String, number: String, password: String) {
            try {
                val db = this.writableDatabase
                val values = ContentValues().apply {
                    put(COLUMN_EMAIL, email)
                    put(COLUMN_NAME, name)
                    put(COLUMN_SURNAME, surname)
                    put(COLUMN_NUMBER, number)
                    put(COLUMN_PASSWORD, password)
                }
                db.insertOrThrow(TABLE_NAME, null, values)
                db.close()
            } catch (e: Exception) {
                println("Error inserting user: ${e.message}")
            }
        }


        companion object {
            private const val DATABASE_VERSION = 1
            private const val DATABASE_NAME = "ClinicDB"
            private const val TABLE_NAME = "Users"
            private const val COLUMN_ID = "id"
            private const val COLUMN_EMAIL = "user_email"
            private const val COLUMN_NAME = "user_name"
            private const val COLUMN_SURNAME = "user_surname"
            private const val COLUMN_NUMBER = "phone_number"
            private const val COLUMN_PASSWORD = "user_password"
        }
    }
}

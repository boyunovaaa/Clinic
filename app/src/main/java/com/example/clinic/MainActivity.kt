package com.example.clinic

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var dbHelper: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.authorization)

        emailEditText = findViewById(R.id.EmailAddress)
        passwordEditText = findViewById(R.id.password)

        dbHelper = DBHelper(this)

        findViewById<Button>(R.id.entrance).setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (checkUser(email, password)) {
                val intent = Intent(this, IndicatorActivity::class.java)
                intent.putExtra("userEmail", email)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Неверный email или пароль", Toast.LENGTH_SHORT).show()
            }
        }

        val registrationTextView = findViewById<TextView>(R.id.registration)
        registrationTextView.setOnClickListener {
            val intent = Intent(this, RegistActivity::class.java)
            startActivity(intent)
        }
    }


    private fun checkUser(email: String, password: String): Boolean {
        val db = dbHelper.readableDatabase
        val columns = arrayOf(DBHelper.COLUMN_PASSWORD)
        val selection = "${DBHelper.COLUMN_EMAIL} = ?"
        val selectionArgs = arrayOf(email)

        val cursor: Cursor = db.query(DBHelper.TABLE_NAME, columns, selection, selectionArgs, null, null, null)
        var userExists = false

        if (cursor != null && cursor.moveToFirst()) {
            val columnIndex = cursor.getColumnIndex(DBHelper.COLUMN_PASSWORD)
            if (columnIndex >= 0) {
                val storedPassword = cursor.getString(columnIndex)
                val hashedPassword = PasswordHasher.hashPassword(password)
                userExists = storedPassword == hashedPassword
            }
        }

        cursor.close()
        return userExists
    }

    class DBHelper(context: Context) :
        SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

        override fun onCreate(db: SQLiteDatabase) {
            val createTable = "CREATE TABLE $TABLE_NAME ($COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "$COLUMN_EMAIL TEXT, $COLUMN_NAME TEXT, $COLUMN_SURNAME TEXT, $COLUMN_NUMBER TEXT, $COLUMN_PASSWORD TEXT)"
            db.execSQL(createTable)
        }


        override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
            db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
            onCreate(db)
        }

        companion object {
            const val DATABASE_VERSION = 1
            const val DATABASE_NAME = "ClinicDB"
            const val TABLE_NAME = "Users"
            const val COLUMN_ID = "id"
            const val COLUMN_EMAIL = "user_email"
            const val COLUMN_NAME = "user_name"
            const val COLUMN_SURNAME = "user_surname"
            const val COLUMN_NUMBER = "phone_number"
            const val COLUMN_PASSWORD = "user_password"
        }
    }
}

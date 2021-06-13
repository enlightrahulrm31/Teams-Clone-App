package com.example.bcd



import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sign_in_button = findViewById<Button>(R.id.signup)
        val sign_up_button = findViewById<Button>(R.id.signin)
        sign_in_button.setOnClickListener{
            Toast.makeText(this, "SIGN UP.", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
        sign_up_button.setOnClickListener{
            Toast.makeText(this, "SIGN IN.", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }

    }
}
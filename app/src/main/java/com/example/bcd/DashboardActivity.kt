package com.example.bcd

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView

class DashboardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        val username:String? =getIntent().getStringExtra("UserName")
        val useremailid:String? =getIntent().getStringExtra("UserEmailid")
        val n = findViewById<TextView>(R.id.Uname)
        val e = findViewById<TextView>(R.id.Uemail)
         n.setText(username)
         e.setText(useremailid)



    }
}
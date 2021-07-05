package com.example.bcd

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_destination.*
class DestinationActivity : AppCompatActivity() {
    private  lateinit var binding: AlarmReciever
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_destination)
        buttonstop.setOnClickListener {
             binding.r.stop()
            map[namemap]=null
        }
    }
}
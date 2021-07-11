package com.example.bcd.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.bcd.Adapters.map
import com.example.bcd.Adapters.namemap
import com.example.bcd.Reciever.AlarmReciever
import com.example.bcd.R
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
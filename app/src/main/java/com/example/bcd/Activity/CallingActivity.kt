package com.example.bcd.Activity

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.bcd.R
import kotlinx.android.synthetic.main.activity_calling.*

class CallingActivity : AppCompatActivity() {
    var phoneno :String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_calling)

        var curUserUrl :String = intent.getStringExtra("photourl")!!

        textname.text = intent.getStringExtra("sendername")

        phoneno = intent.getStringExtra("phonennumber")!!

        Glide      // user to download url of the image
            .with(this)
            .load(curUserUrl)
            .centerCrop()
            .placeholder(R.drawable.ic_baseline_person_24)
            .into(calluserimage)

        buttoncall.setOnClickListener {
            makephonecall()
        }
    }

    fun makephonecall() {
        val number: String = phoneno
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, Array<String>(1) { android.Manifest.permission.CALL_PHONE }, 1)
        } else {
            if (number.length > 0) {
                val dial: String = "tel:" + number
                startActivity(Intent(Intent.ACTION_CALL, Uri.parse(dial)))
            }

        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if(requestCode == 1){
            if(grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                makephonecall()
            }
            else{
                Toast.makeText(this,"permission granted", Toast.LENGTH_SHORT).show()
            }

        }
        else{
            Toast.makeText(this,"ENTER NNUMBER", Toast.LENGTH_SHORT).show()
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}
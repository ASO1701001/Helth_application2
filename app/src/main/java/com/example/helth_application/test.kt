package com.example.helth_application

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_test.*

class test : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        val birthDay = intent.getStringExtra("Birth_Day")
        val weight = intent.getStringExtra("Weight")

        this.textView2.text = birthDay
        textView6.text = weight
    }
}

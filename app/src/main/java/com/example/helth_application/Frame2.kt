package com.example.helth_application

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_frame2.*


class Frame2 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_frame2)

        //Next_pageボタンのメソッドを実行
        Next_page.setOnClickListener{
            onNextButtonTapped()
        }
    }
    //ページ遷移
    //パーソナルデータページへ
    fun onNextButtonTapped(){
        var intent = Intent(this, Frame2_2::class.java)
        startActivity(intent)
    }
}

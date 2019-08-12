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
        Next_page.setOnClickListener {
            onNextButtonTapped()
            data_substitution1()
        }
    }
    //ページ遷移
    //パーソナルデータページへ
    fun onNextButtonTapped(){
        var intent = Intent(this, Frame2_2::class.java)
        startActivity(intent)
    }

    //フォームデータ
    fun data_substitution1(){
        var userId = Reg_ID.text.toString()            //userid
        var userPass = Reg_password.text.toString()   //password
        var confirm = Reg_comfirm.text.toString()     //confirm

    }
}

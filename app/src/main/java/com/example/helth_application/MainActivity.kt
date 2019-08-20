package com.example.helth_application

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //新規作成ボタンタップで実行
        Make_acount.setOnClickListener{
            this.onRegistrationButtonTapped()
        }

        //Loginボタンタップで実行
        Login.setOnClickListener{
            this.onLoginButtonTapped()
        }
    }

    //画面遷移
    //登録画面へ遷移
    fun onRegistrationButtonTapped(){
        var intent = Intent(this, Frame2::class.java)
        startActivity(intent)
    }

    //ログイン画面へ遷移
    fun onLoginButtonTapped(){
        var intent = Intent(this,Frame3::class.java)
        startActivity(intent)
    }


}

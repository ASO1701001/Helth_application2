package com.example.helth_application

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_frame2.*
import kotlinx.android.synthetic.main.activity_frame2_2.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.Make_acount
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.activity_test.*

class Frame2_2 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_frame2_2)

        //Make_accountボタンのメソッドを実行
        Make_acount.setOnClickListener{
            data_substitution2()
            onMakeButtonTapped()
        }
    }

    //ページ遷移
    //パーソナルデータページへ
    fun onMakeButtonTapped() {
        var intent = Intent(this, test::class.java)
        startActivity(intent)
    }
    fun data_substitution2() {
        var birthDay = Birthday_form.text.toString()    //生年月日
        val weight = weight_form.text                 //体重
        val height = hight_form.text                   //身長
        val colorGroup: RadioGroup = findViewById(R.id.select_sex)
        val sex = colorGroup.setOnCheckedChangeListener {_, checkedId: Int ->
            when (checkedId) {
                R.id.radio_man -> Toast.makeText(this, "男性", Toast.LENGTH_SHORT).show()
                R.id.radio_woman -> Toast.makeText(this, "女性", Toast.LENGTH_SHORT).show()
                else -> throw IllegalArgumentException("not supported")
            }
        }

        textView2.text = birthDay
        textView6.text = weight
        textView7.text = height
        textView8.text = sex.toString()
    }
}

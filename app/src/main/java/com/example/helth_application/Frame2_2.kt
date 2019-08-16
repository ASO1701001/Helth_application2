package com.example.helth_application

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import com.google.android.material.snackbar.Snackbar

import kotlinx.android.synthetic.main.activity_frame2_2.*
import kotlinx.android.synthetic.main.activity_main.Make_acount
import java.util.regex.Pattern


class Frame2nd : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Make_accountボタンのメソッドを実行
        Make_acount.setOnClickListener {
            view ->signUp(view)
           // dataSubstitution2(view: View)
            onMakeButtonTapped()
        }
    }


    //ページ遷移
    //パーソナルデータページへ
    fun onMakeButtonTapped() {
        val intent = Intent(this, test::class.java)
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()

        Make_acount.setOnClickListener { view ->
            signUp(view)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()

        return true
    }

    private fun validationBirthday(): Boolean {
        val birthDay = Birthday_form.editableText.toString().trim()

        return when {
            birthDay.isEmpty() -> {
                Birthday_form.error = "ユーザー名が入力されていません"
                false
            }
            !Pattern.compile("^.{1,30}\$").matcher(birthDay).find() -> {
                Birthday_form.error = "最大文字数をオーバーしています"
                false
            }
            else -> {
                Birthday_form.error = null
                true
            }
        }
    }

    private fun validationWeight(): Boolean {
        val weight = weight_form.editableText.toString().trim()

        return when {
            weight.isEmpty() -> {
                weight_form.error = "メールアドレスが入力されていません"
                false
            }
            else -> {
                weight_form.error = null
                true
            }
        }
    }

    private fun validationHight(): Boolean {
        val hight = hight_form.editableText.toString().trim()

        return when {
            hight.isEmpty() -> {
                hight_form.error = "パスワードが入力されていません"
                false
            }
            else -> {
                hight_form.error = null
                true
            }
        }


    }

    private fun validationSex(): Boolean {
        val sex = select_sex.checkedRadioButtonId.toString().trim()

        return when {
            sex.isEmpty() -> {
                false
            }
            else -> {
                true
            }
        }


    }


    fun signUp(view: View) {
    var check = true
    if (!validationBirthday()) check = false
    if (!validationWeight()) check = false
    if (!validationHight()) check = false
    if (!validationSex()) check = false

    if (!check) return

    ApiPostTask {
        if (it == null) {
            Snackbar.make(view, "APIとの通信に失敗しました", Snackbar.LENGTH_SHORT).show()
        } else {
            when (it.getString("status")) {
                "S00" -> {
                    val token = it.getJSONObject("data").getString("token")
                    val editor = getSharedPreferences("data", MODE_PRIVATE).edit()
                    editor.putString("token", token).apply()
                    startActivity(
                        Intent(
                            this, MainActivity::class.java
                        ).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    )
                }
                "E00" -> {
                    val msgArray = it.getJSONArray("msg")
                    for (i in 0 until msgArray.length()) {
                        when (msgArray.getString(i)) {
                            "REQUIRED_PARAM" -> Snackbar.make(view, "必要な値が見つかりませんでした", Snackbar.LENGTH_SHORT).show()
                            "VALIDATION_USER_ID" -> Birthday_form.error = "生年月日の入力規則に違反しています"
                            "VALIDATION_USER_NAME" -> weight_form.error = "体重の入力規則に違反しています"
                            "VALIDATION_EMAIL" -> hight_form.error = "身長の入力規則に違反しています"
                            else -> Snackbar.make(view, "不明なエラーが発生しました", Snackbar.LENGTH_SHORT).show()
                        }
                    }
                }
                else -> Snackbar.make(view, "不明なエラーが発生しました", Snackbar.LENGTH_SHORT).show()
            }
        }
    }.execute(
        ApiParams(
            "account/sign-up",
            hashMapOf(
                "birthday" to Birthday_form.editableText.toString(),
                "weight" to weight_form.editableText.toString(),
                "height" to hight_form.editableText.toString(),
                "sex" to select_sex.checkedRadioButtonId.toString()
            )
        )
    )
}

}

package com.example.helth_application

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_frame2.*
import java.util.regex.Pattern


class Frame2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_frame2)
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeButtonEnabled(true)
        } ?: IllegalAccessException("Toolbar cannot be null")
        title = resources.getString(R.string.Reg_title)
        onNextButtonTapped()

    }
    //ページ遷移
    //パーソナルデータページへ
    fun onNextButtonTapped(){
        if (Reg_password.text.toString() !== Reg_comfirm.text.toString()) {

        } else{
            val intent = Intent(this, Frame2nd::class.java)
            intent.putExtra("user_id", Reg_ID.text.toString())
            intent.putExtra("password", Reg_password.text.toString())
            intent.putExtra("confirm",Reg_password.text.toString())
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()

        Next_page.setOnClickListener { view ->
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

    private fun validationUserId(): Boolean {
        val userId = Reg_ID.text.toString().trim()

        return when {
            userId.isEmpty() -> {
                Reg_ID.error = "ユーザーIDが入力されていません"
                false
            }
            !Pattern.compile("^[a-zA-Z0-9-_]{4,30}\$").matcher(userId).find() -> {
                Reg_ID.error = "4文字以上30文字以下の半角英数字で入力してください"
                false
            }
            else -> {
                Reg_ID.error = null
                true
            }
        }
    }

    private fun validationPassword(): Boolean {
        val password = Reg_password.text.toString().trim()

        return when {
            password.isEmpty() -> {
                Reg_password.error = "パスワードが入力されていません"
                false
            }
            else -> {
                Reg_password.error = null
                true
            }
        }
    }

    private fun validationConfirm(): Boolean {
        val confirm = Reg_comfirm.text.toString().trim()

        return when {
            confirm.isEmpty() -> {
                Reg_comfirm.error = "メールアドレスが入力されていません"
                false
            }
            else -> {
                Reg_comfirm.error = null
                true
            }
        }
    }

    private fun signUp(view: View) {
        var check = true
        if (!validationUserId()) check = false
        if (!validationConfirm()) check = false
        if (!validationPassword()) check = false

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
                                "VALIDATION_USER_ID" -> Reg_ID.error = "ユーザーIDの入力規則に違反しています"
                                "VALIDATION_PASSWORD" -> Reg_password.error = "パスワードの入力規則に違反しています"
                                "VALIDATION_CONFIRM" -> Reg_comfirm.error = "メールアドレスの入力規則に違反しています"
                                "ALREADY_USER_ID" -> Reg_ID.error = "入力されたユーザーは既に登録されています"
                                "DISTINCT_PASS" -> Reg_password.error = "入力されたメールアドレスは既に登録されています"
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
                    "user_id" to Reg_ID.editableText.toString(),
                    "password" to Reg_password.editableText.toString(),
                    "confirm" to Reg_comfirm.editableText.toString()
                )
            )
        )
    }

}


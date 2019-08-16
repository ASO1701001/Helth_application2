package com.example.helth_application

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_frame3.*
import java.util.regex.Pattern

class Frame3 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_frame3)

        //Loginボタンのメソッドを実行
        Login_button.setOnClickListener { view ->
            signIn(view)
        }
    }


    //ページ遷移
    //パーソナルデータページへ
    fun onLoginButtonTapped() {
        val intent = Intent(this, test::class.java)
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()

    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()

        return true
    }

    private fun validationUseId(): Boolean {
        val userId = Login_ID.editableText.toString().trim()

        return when {
            userId.isEmpty() -> {
                Login_ID.error = "ユーザーIDが入力されていません"
                false
            }
            !Pattern.compile("^.{1,30}\$").matcher(userId).find() -> {
                Login_ID.error = "最大文字数をオーバーしています"
                false
            }
            else -> {
                Login_ID.error = null
                true
            }
        }
    }

    private fun validationPassWord(): Boolean {
        val passWord = Login_password.editableText.toString().trim()

        return when {
            passWord.isEmpty() -> {
                Login_password.error = "メールアドレスが入力されていません"
                false
            }
            else -> {
                Login_password.error = null
                true
            }
        }
    }

    fun signIn(view: View) {
        var check = true
        if (!validationUseId()) check = false
        if (!validationPassWord()) check = false
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
                                "VALIDATION_USER_ID" -> Login_ID.error = "ユーザーIDの入力規則に違反しています"
                                "VALIDATION_USER_PASSWORD" -> Login_password.error = "パスワードの入力規則に違反しています"
                                else -> Snackbar.make(view, "不明なエラーが発生しました", Snackbar.LENGTH_SHORT).show()
                            }
                        }
                    }
                    else -> Snackbar.make(view, "不明なエラーが発生しました", Snackbar.LENGTH_SHORT).show()
                }
            }
        }.execute(
            ApiParams(
                "account/sign_in.php",
                hashMapOf(
                    "user_id" to Login_ID.editableText.toString(),
                    "password" to Login_password.editableText.toString()
                )
            )
        )
    }

}

package com.example.helth_application

class ApiParams(
    apiName: String,
    val params: HashMap<String, String> = hashMapOf()
) {
    val api = "https://main-eve.ssl-lolipop.jp/hacku2019/api/$apiName"
}
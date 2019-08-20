package com.example.helth_application

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.fitness.Fitness
import com.google.android.gms.fitness.FitnessOptions
import com.google.android.gms.fitness.data.DataType
import com.google.android.gms.fitness.request.DataReadRequest
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_frame6.*
import java.util.*
import java.util.concurrent.TimeUnit

class Frame6 : AppCompatActivity() {

    private  val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener {
            item -> when (item.itemId) {
                    R.id.sleep -> {//睡眠アイコンタップ時
                        // val intent = Intent(this, FrameSleep::class.java)
                        startActivity(intent)
                        return@OnNavigationItemSelectedListener true
                    }

                    R.id.food -> {//食事アイコン
                        val intent = Intent(this,Frame7::class.java)
                        startActivity(intent)
                    }

                    R.id.nutrion -> {//栄養アイコン
                        // val intent = Intent(this,Frame8::class.java
                        startActivity(intent)
                    }

                    R.id.settings -> {///設定アイコン
                        //val intent = Intent(this,Frame9::class.java)
                        startActivity(intent)
                    }

                    else ->

            }
        }
    }

       private val GOOGLE_FIT_PERMISSIONS_REQUEST_CODE = 1000
       private val LOG_TAG = "TEST"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_frame6)

        val fitnessOptions = FitnessOptions.builder()
            .addDataType(DataType.TYPE_STEP_COUNT_DELTA,FitnessOptions.ACCESS_READ)
            .addDataType(DataType.AGGREGATE_STEP_COUNT_DELTA,FitnessOptions.ACCESS_READ)
            .build()

        if(!GoogleSignIn.hasPermissions(GoogleSignIn.getLastSignedInAccount(this),fitnessOptions)){
            GoogleSignIn.requestPermissions(
            this,
            GOOGLE_FIT_PERMISSIONS_REQUEST_CODE,
            GoogleSignIn.getLastSignedInAccount(this),
            fitnessOptions
            )
        }else{
            accessGoogleFit()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == GOOGLE_FIT_PERMISSIONS_REQUEST_CODE){
                accessGoogleFit()
            }
        }
    }

    private fun accessGoogleFit() {
        val cal = Calendar.getInstance()
        cal.time = Date()
        val endTime = cal.timeInMillis
        cal.add(Calendar.YEAR, -1)
        val startTime = cal.timeInMillis

        val readRequest = DataReadRequest.Builder()
            .aggregate(DataType.TYPE_STEP_COUNT_DELTA,DataType.AGGREGATE_STEP_COUNT_DELTA)
            .setTimeRange(startTime,endTime,TimeUnit.MILLISECONDS).build()

        Fitness.getHistoryClient(this,GoogleSignIn.getLastSignedInAccount(this)!!)
            .readData(readRequest)
            .addOnSuccessListener {
                Log.d(LOG_TAG,"onSuccess()")
            }
            .addOnFailureListener{
                e -> Log.e(LOG_TAG,"onFailure()", e)
            }
            .addOnCompleteListener {
                Log.d(LOG_TAG,"onComplete()")
            }
    }
}

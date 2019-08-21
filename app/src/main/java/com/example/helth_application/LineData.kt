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
import com.google.android.gms.fitness.data.Field
import com.google.android.gms.fitness.request.DataReadRequest
import java.util.*
import java.util.concurrent.TimeUnit

class LineData : AppCompatActivity(){
    private val GOOGLE_FIT_PERMISSIONS_REQUEST_CODE = 1000
    private val LOG_TAG = "TEST"

    override  fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_frame6)

        val fitnessOptions = FitnessOptions.builder()
            .addDataType(DataType.TYPE_STEP_COUNT_DELTA,FitnessOptions.ACCESS_READ)
            .addDataType(DataType.AGGREGATE_STEP_COUNT_DELTA,FitnessOptions.ACCESS_READ)
            .build()

        if(!GoogleSignIn.hasPermissions(GoogleSignIn.getLastSignedInAccount(this),fitnessOptions)){
            GoogleSignIn.requestPermissions(
                this,GOOGLE_FIT_PERMISSIONS_REQUEST_CODE,
                GoogleSignIn.getLastSignedInAccount(this),fitnessOptions
            )
        }else{
            accessGoogleFit()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode == Activity.RESULT_OK){
            if(requestCode == GOOGLE_FIT_PERMISSIONS_REQUEST_CODE){
                accessGoogleFit()
            }
        }
    }

    private fun accessGoogleFit(){
        val cal = Calendar.getInstance()
        cal.time = Date()
        val end = cal.timeInMillis
        cal.add(Calendar.YEAR,-1)
        val start = 1544540400000 // 2018-12-12 00:00:00

        val readRequest = DataReadRequest.Builder()
            .aggregate(DataType.TYPE_STEP_COUNT_DELTA, DataType.AGGREGATE_STEP_COUNT_DELTA)
            .setTimeRange(start, end, TimeUnit.MILLISECONDS)
            .bucketByTime(1, TimeUnit.DAYS) // 集計間隔を1日毎に指定
            .build()

        Fitness.getHistoryClient(this,GoogleSignIn.getLastSignedInAccount(this)!!)
            .readData(readRequest)
            .addOnSuccessListener {
                val buckets = it.buckets // 集計データはbucketsというところに入ってくる
                buckets.forEach { bucket ->
                    val start = bucket.getStartTime(TimeUnit.MILLISECONDS)
                    val end = bucket.getEndTime(TimeUnit.MILLISECONDS)
                    val dataSet = bucket.getDataSet(DataType.AGGREGATE_STEP_COUNT_DELTA)
                    val value = dataSet!!.dataPoints.first().getValue(Field.FIELD_STEPS)
                    Log.d("Aggregate", "$start $end $value")
                }
            }
            .addOnFailureListener{e -> Log.e(LOG_TAG,"onFailure()",e)}
            .addOnCompleteListener { Log.d(LOG_TAG,"onComplete()") }
    }
}
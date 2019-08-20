package com.example.helth_application

import android.app.Activity
import android.app.FragmentManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.util.Log
import android.view.MenuItem
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.fitness.Fitness
import com.google.android.gms.fitness.FitnessOptions
import com.google.android.gms.fitness.data.DataType
import com.google.android.gms.fitness.request.DataReadRequest
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomnavigation.BottomNavigationView.OnNavigationItemSelectedListener
import kotlinx.android.synthetic.main.activity_frame6.*
import java.util.*
import java.util.concurrent.TimeUnit

class Frame6 : AppCompatActivity() {


    object LogicValues {
        const val Fragment1 = 0
        const val Fragment2 = 1
        const val Fragment3 = 2
    }

    private var currentItem: MenuItem? = null

    private  val onNavigationItemSelectedListener = OnNavigationItemSelectedListener {
            item -> when (item.itemId) {
                    R.id.sleep -> {//睡眠アイコンタップ時
                        // val intent = Intent(this, FrameSleep::class.java)
                        //startActivity(intent)
                        ViewPager.setCurrentItem(LogicValues.Fragment1, false)
                        return@OnNavigationItemSelectedListener true
                    }

                    R.id.food -> {//食事アイコン
                        //val intent = Intent(this,Frame7::class.java)
                        //startActivity(intent)
                        return@OnNavigationItemSelectedListener true
                    }

                    R.id.nutrion -> {//栄養アイコン
                        // val intent = Intent(this,Frame8::class.java
                        //startActivity(intent)
                        return@OnNavigationItemSelectedListener true
                    }

                    R.id.settings -> {///設定アイコン
                        //val intent = Intent(this,Frame9::class.java)
                        //startActivity(intent)
                        return@OnNavigationItemSelectedListener true
                    }
            }
            false
        }

    //this.setoNavigationItemSelectedListener(onNavigationItemSelectedListener)

    class ViewPagerAdapter(private val fragment: List<Fragment>,
        fragmentManager: androidx.fragment.app.FragmentManager) : FragmentPagerAdapter(fragmentManager) {

        override fun getCount() = fragment.size

        override fun getItem(position: Int): Fragment {
            return fragment[position]
        }

    }

    class BottomNavigationViewPager : ViewPager {
        constructor(context: Context) : super(context)
        constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

        override fun onInterceptHoverEvent(event: MotionEvent?): Boolean {
            return false
        }

        override fun onTouchEvent(ev: MotionEvent?): Boolean {
            return false
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

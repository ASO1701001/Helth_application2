package com.example.helth_application

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.MenuItem
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.helth_application.Frame6.LogicValues.Fragment1
import com.example.helth_application.Frame6.LogicValues.Fragment2
import com.example.helth_application.Frame6.LogicValues.Fragment3
import com.example.helth_application.Frame6.LogicValues.Fragment4
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.fitness.Fitness
import com.google.android.gms.fitness.FitnessOptions
import com.google.android.gms.fitness.data.DataType
import com.google.android.gms.fitness.request.DataReadRequest
import com.google.android.material.bottomnavigation.BottomNavigationView.OnNavigationItemSelectedListener
import kotlinx.android.synthetic.main.activity_frame6.*
import java.util.*
import java.util.concurrent.TimeUnit

class Frame6 : AppCompatActivity() {

    object LogicValues {
        const val Fragment1 = 0
        const val Fragment2 = 1
        const val Fragment3 = 2
        const val Fragment4 = 3
    }

    private var currentItem: MenuItem? = null

    private val onNavigationItemSelectedListener = OnNavigationItemSelectedListener {
            item -> when (item.itemId) {
                    R.id.sleep -> {//睡眠アイコンタップ時
                    // val intent = Intent(this, FrameSleep::class.java)
                    //startActivity(intent)
                    viewPager.setCurrentItem(Fragment1, false)
                    return@OnNavigationItemSelectedListener true
                }

                R.id.food -> {//食事アイコン
                    //val intent = Intent(this,Frame7::class.java)
                    //startActivity(intent)
                    viewPager.setCurrentItem(Fragment2, false)
                    return@OnNavigationItemSelectedListener true
                }

                R.id.nutrion -> {//栄養アイコン
                    // val intent = Intent(this,Frame8::class.java
                    //startActivity(intent)
                    viewPager.setCurrentItem(Fragment3, false)
                    return@OnNavigationItemSelectedListener true
                }

                R.id.settings -> {///設定アイコン
                    //val intent = Intent(this,Frame9::class.java)
                    //startActivity(intent)
                    viewPager.setCurrentItem(Fragment4, false)
                    return@OnNavigationItemSelectedListener true
                }
        }
        false
    }

    private fun setupViewPager() {
        val fragment = mutableListOf(Fragment1, Fragment2, Fragment3, Fragment4)
        val adapter = ViewPagerAdapter(fragment, supportFragmentManager)
        v.adapter = adapter
        viewPager.offscreenPageLimit = 2

        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
            override fun onPageScrollStateChanged(state: Int) {}
            override fun onPageSelected(position: Int) {
                if (currentItem != null) {
                    (currentItem as MenuItem).isChecked = false
                } else {
                    BottomMenu.menu.getItem(0).isChecked = false
                }
                BottomMenu.menu.getItem(position).isChecked = true
                currentItem = BottomMenu.menu.getItem(position)
            }
        })
    }


    class ViewPagerAdapter(private val fragment: MutableList<Int>,
        fragmentManager: androidx.fragment.app.FragmentManager) : FragmentPagerAdapter(fragmentManager) {
        override fun getCount() = fragment.size
        override fun getItem(position: Int): Int {
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
        setupViewPager()
        BottomMenu.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)

        val fitnessOptions = FitnessOptions.builder()
            .addDataType(DataType.TYPE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
            .addDataType(DataType.AGGREGATE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
            .build()

        if (!GoogleSignIn.hasPermissions(GoogleSignIn.getLastSignedInAccount(this), fitnessOptions)) {
            GoogleSignIn.requestPermissions(
                this,
                GOOGLE_FIT_PERMISSIONS_REQUEST_CODE,
                GoogleSignIn.getLastSignedInAccount(this),
                fitnessOptions
            )
        } else {
            accessGoogleFit()
        }
    }


    @SuppressLint("MissingSuperCall")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == GOOGLE_FIT_PERMISSIONS_REQUEST_CODE) {
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
            .aggregate(DataType.TYPE_STEP_COUNT_DELTA, DataType.AGGREGATE_STEP_COUNT_DELTA)
            .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS).build()

        Fitness.getHistoryClient(this, GoogleSignIn.getLastSignedInAccount(this)!!)
            .readData(readRequest)
            .addOnSuccessListener {
                Log.d(LOG_TAG, "onSuccess()")
            }
            .addOnFailureListener { e ->
                Log.e(LOG_TAG, "onFailure()", e)
            }
            .addOnCompleteListener {
                Log.d(LOG_TAG, "onComplete()")
            }
    }
}

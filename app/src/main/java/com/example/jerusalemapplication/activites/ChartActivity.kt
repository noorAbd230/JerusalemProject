package com.example.jerusalemapplication.activites

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
import com.example.jerusalemapplication.R
import com.example.jerusalemapplication.model.SharedPref
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_chart.*
import kotlinx.android.synthetic.main.activity_library_details.*

class ChartActivity : AppCompatActivity() {
    lateinit var firebaseAnalytics: FirebaseAnalytics
    lateinit var sharedPref: SharedPref

    override fun onCreate(savedInstanceState: Bundle?) {
        sharedPref = SharedPref(this)
        if (sharedPref.loadNightModeState()){
            setTheme(R.style.DarkTheme)
        }else{
            setTheme(R.style.AppTheme)
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chart)

        firebaseAnalytics = Firebase.analytics
        trackScreen("Chat Screen")
        var actionBar = supportActionBar
        actionBar!!.title = resources.getString(R.string.app_name)

        var visitors = ArrayList<BarEntry>()
        visitors.add(BarEntry(2013F, 95F))
        visitors.add(BarEntry(2014F, 51F))
        visitors.add(BarEntry(2015F, 47F))
        visitors.add(BarEntry(2016F, 88F))
        visitors.add(BarEntry(2017F, 61F))
        visitors.add(BarEntry(2018F, 57F))
        visitors.add(BarEntry(2019F, 169F))
        var barDataSet = BarDataSet(visitors, "عمليات الهدم")
        barDataSet.setColors(*ColorTemplate.MATERIAL_COLORS)
        barDataSet.valueTextColor = Color.BLACK
        barDataSet.valueTextSize = 16F
        var bardata = BarData(barDataSet)
        bar_chart.setFitBars(true)
        bar_chart.data = bardata
        bar_chart.description.text = "عمليات الهدم في القدس"
        bar_chart.animateY(2000)

        var ourFontSize = 14f

        chart_plus.setOnClickListener {
            ourFontSize+=4f
            chart_details.setTextSize(TypedValue.COMPLEX_UNIT_SP,ourFontSize)

        }
        chart_mines.setOnClickListener {
            ourFontSize-=4f
            chart_details.setTextSize(TypedValue.COMPLEX_UNIT_SP,ourFontSize)
        }
    }


    private fun trackScreen(screenName:String) {
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_NAME, screenName)
            param(FirebaseAnalytics.Param.SCREEN_CLASS, "MainActivity")
        }

    }
}
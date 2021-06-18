package com.example.jerusalemapplication.activites

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Switch
import androidx.appcompat.app.AlertDialog
import com.example.jerusalemapplication.R
import com.example.jerusalemapplication.model.SharedPref
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_setting.*
import java.util.*

class SettingActivity : AppCompatActivity() {
    private var switch: Switch? = null
    lateinit var sharedPref: SharedPref
    lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        sharedPref = SharedPref(this)
        if (sharedPref.loadNightModeState()){
            setTheme(R.style.DarkTheme)
        }else{
            setTheme(R.style.AppTheme)
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        loadLoacle()

        var actionBar = supportActionBar
        actionBar!!.title = resources.getString(R.string.app_name)



        firebaseAnalytics = Firebase.analytics
        trackScreen("Setting Screen")

        changeLanguage.setOnClickListener {
            showChangeLanguageDialog()
        }
        switch = findViewById<View>(R.id.enableDark) as Switch?
        if (sharedPref.loadNightModeState()){
            switch!!.isChecked = true
        }
        switch!!.setOnCheckedChangeListener { compoundButton, b ->
            if (b) {
                sharedPref.setNightModeState(true)
                restartApp()
            } else {
                sharedPref.setNightModeState(false)
                restartApp()
            }
        }
    }

    private fun restartApp() {
        startActivity(Intent(applicationContext, SettingActivity::class.java))
        finish()
    }

    fun showChangeLanguageDialog(){
        var listItems = arrayOf<String>("عربي","English","French")


        var mBuilder = AlertDialog.Builder(this)
        mBuilder.setTitle("اختر اللغة..")

        mBuilder.setSingleChoiceItems(listItems,-1,DialogInterface.OnClickListener() { dialogInterface, i ->
            if (i == 0) {
                setLocale("ar")
                recreate()

            }else
            if (i == 1) {
                setLocale("en")
                recreate()

            }else if (i == 2) {
                setLocale("fr")
                recreate()

            }


            dialogInterface.dismiss()

        })

        var mDialog =  mBuilder.create()
        mDialog.show()
    }

    private fun trackScreen(screenName:String) {
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_NAME, screenName)
            param(FirebaseAnalytics.Param.SCREEN_CLASS, "MainActivity")
        }

    }

    private fun setLocale(lang:String) {

        val locale= Locale(lang)
        Locale.setDefault(locale)
        val config= Configuration()
        config.locale=locale
        baseContext.resources.updateConfiguration(config, baseContext.resources.displayMetrics)

        var editor=getSharedPreferences("Settings", MODE_PRIVATE).edit()
        editor.putString("My_Lang", lang)
        editor.apply()

    }

    private fun loadLoacle(){
        var prefs=getSharedPreferences("Settings", Activity.MODE_PRIVATE)
        val language:String? = prefs.getString("My_Lang","")
        println("RESULT$language")
        setLocale(language!!)
    }
}
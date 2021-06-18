package com.example.jerusalemapplication.activites

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
import com.example.jerusalemapplication.R
import com.example.jerusalemapplication.model.SharedPref
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_library_details.*
import kotlinx.android.synthetic.main.activity_news_details.*

class NewsDetailsActivity : AppCompatActivity() {
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
        setContentView(R.layout.activity_news_details)

        var title = intent.getStringExtra("title")
        var img = intent.getStringExtra("img")
        var desc = intent.getStringExtra("desc")
        var date = intent.getStringExtra("date")
        var url = intent.getStringExtra("url")
        var actionBar = supportActionBar
        actionBar!!.title = resources.getString(R.string.app_name)
        Picasso.get().load(img).into(headerImg)
        txt_title.text = title
        txt_date.text = date
        txt_desc.text = desc
        txt_url.setOnClickListener {
            goToUrl(url)
        }
        firebaseAnalytics = Firebase.analytics
        trackScreen("News Details Screen")

        var ourFontSize = 14f

        news_plus.setOnClickListener {
            ourFontSize+=4f
            txt_desc.setTextSize(TypedValue.COMPLEX_UNIT_SP,ourFontSize)

        }
        news_mines.setOnClickListener {
            ourFontSize-=4f
            txt_desc.setTextSize(TypedValue.COMPLEX_UNIT_SP,ourFontSize)
        }
    }

    private fun goToUrl(url: String?) {
        var uri = Uri.parse(url)
        var i = Intent(Intent.ACTION_VIEW,uri)
        startActivity(i)
    }

    private fun trackScreen(screenName:String) {
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_NAME, screenName)
            param(FirebaseAnalytics.Param.SCREEN_CLASS, "MainActivity")
        }

    }
}
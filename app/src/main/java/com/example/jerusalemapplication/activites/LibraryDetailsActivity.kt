package com.example.jerusalemapplication.activites

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
import com.example.jerusalemapplication.R
import com.example.jerusalemapplication.model.SharedPref
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_library_details.*
import kotlinx.android.synthetic.main.activity_news_details.*

class LibraryDetailsActivity : AppCompatActivity() {
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
        setContentView(R.layout.activity_library_details)
        var actionBar = supportActionBar
        actionBar!!.title = resources.getString(R.string.app_name)

        var title = intent.getStringExtra("Title")
        var details = intent.getStringExtra("Details")
        var img = intent.getStringExtra("image")
        Picasso.get().load(img).into(lImg)
        lTitle.text = title
        txt_details.text = details
        share.setOnClickListener {view ->
            Snackbar.make(view, "$details", Snackbar.LENGTH_LONG)
                .setAction("شارك المعلومات", null).show()

            val sendIntent = Intent()
            sendIntent.action = Intent.ACTION_SEND
            sendIntent.putExtra(Intent.EXTRA_TEXT, "القدس : \n $details")
            sendIntent.type = "text/plain"
            startActivity(sendIntent)
            finish()
        }

        firebaseAnalytics = Firebase.analytics
        trackScreen("Library Details Screen")
         var ourFontSize = 14f

        plus.setOnClickListener {
            ourFontSize+=4f
            txt_details.setTextSize(TypedValue.COMPLEX_UNIT_SP,ourFontSize)

        }
        mines.setOnClickListener {
            ourFontSize-=4f
            txt_details.setTextSize(TypedValue.COMPLEX_UNIT_SP,ourFontSize)
        }

    }

    private fun trackScreen(screenName:String) {
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_NAME, screenName)
            param(FirebaseAnalytics.Param.SCREEN_CLASS, "MainActivity")
        }

    }
}
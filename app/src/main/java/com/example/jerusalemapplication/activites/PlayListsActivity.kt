package com.example.jerusalemapplication.activites

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import com.example.jerusalemapplication.R
import com.example.jerusalemapplication.adapter.PlayListsAdapter
import com.example.jerusalemapplication.model.PlayList
import com.example.jerusalemapplication.model.SharedPref
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_play_lists.*

class PlayListsActivity : AppCompatActivity() {
    lateinit var db: FirebaseFirestore
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
        setContentView(R.layout.activity_play_lists)
        var actionBar = supportActionBar
        actionBar!!.title = resources.getString(R.string.app_name)
        db= Firebase.firestore
        getAllPlayList()
        firebaseAnalytics = Firebase.analytics
        trackScreen("PlayLists Screen")
    }


    private fun getAllPlayList(){
        val playList=mutableListOf<PlayList>()

        db.collection("playList")
            .get()
            .addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot){

                    playList.add(
                        PlayList(document.getString("img"))
                    )

                }

                rvAllPlayList.layoutManager = GridLayoutManager(this,2)
//                    LinearLayoutManager(this,
//                    LinearLayoutManager.VERTICAL,false)

                rvAllPlayList.setHasFixedSize(true)
                val restAdapter = PlayListsAdapter(this,"PlayListActivity",playList)
                rvAllPlayList.adapter=restAdapter

            }
            .addOnFailureListener { exception ->
                Log.e("nor", exception.message!!)
            }
    }

    private fun trackScreen(screenName:String) {
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_NAME, screenName)
            param(FirebaseAnalytics.Param.SCREEN_CLASS, "MainActivity")
        }

    }
}
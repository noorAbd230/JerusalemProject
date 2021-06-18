package com.example.jerusalemapplication.activites

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jerusalemapplication.R
import com.example.jerusalemapplication.adapter.VideosAdapter
import com.example.jerusalemapplication.model.SharedPref
import com.example.jerusalemapplication.model.Video
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_playlist_videos.*

class PlaylistVideosActivity : AppCompatActivity() {
    lateinit var db: FirebaseFirestore
     var img: String? = null
     var title: String? = null
    lateinit var sharedPref: SharedPref
    val videos=mutableListOf<Video>()
    lateinit var firebaseAnalytics: FirebaseAnalytics
    override fun onCreate(savedInstanceState: Bundle?) {

        sharedPref = SharedPref(this)
        if (sharedPref.loadNightModeState()){
            setTheme(R.style.DarkTheme)
        }else{
            setTheme(R.style.AppTheme)
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playlist_videos)
        var actionBar = supportActionBar
        actionBar!!.title = resources.getString(R.string.app_name)

        db= Firebase.firestore
        var from = intent.getStringExtra("from")
         if(from=="home"){
             img = intent.getStringExtra("img")
             title = intent.getStringExtra("title")
        }else{
             img = intent.getStringExtra("gridImg")
             title = intent.getStringExtra("gridTitle")
        }
        playList_video_title.text = title
        getAllVideos()


        firebaseAnalytics = Firebase.analytics
        trackScreen("Playlist Videos Screen")

    }

//    var simpleCallBack =  object : ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.START or ItemTouchHelper.END ,0) {
//        override fun onMove(
//            recyclerView: RecyclerView,
//            viewHolder: RecyclerView.ViewHolder,
//            target: RecyclerView.ViewHolder
//        ): Boolean {
//
//            var fromPosition = viewHolder.adapterPosition
//            var toPosition = target.adapterPosition
//            Collections.swap(videos,fromPosition,toPosition)
//            recyclerView.adapter!!.notifyItemMoved(fromPosition,toPosition)
//            return false
//        }
//
//        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
//            TODO("Not yet implemented")
//        }
//    }


    private fun getAllVideos(){


        db.collection("playList").whereEqualTo("img",img)
            .get()
            .addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot){

                    db.collection("playList").document(document.id).collection("videos")
                        .get()
                        .addOnSuccessListener { querySnapshotv ->
                            for (vDocument in querySnapshotv) {
                                videos.add(
                                    Video(vDocument.getString("img"),vDocument.getString("url"))
                                )
                            }

                            rvVideos.layoutManager =
                                    //GridLayoutManager(this,2)
                                LinearLayoutManager(this,
                                    LinearLayoutManager.VERTICAL,false)

                            rvVideos.setHasFixedSize(true)
                            val restAdapter = VideosAdapter(this, title!!,videos)
                            rvVideos.adapter=restAdapter
//                            var itemTouchHelper = ItemTouchHelper(simpleCallBack)
//                            itemTouchHelper.attachToRecyclerView(rvVideos)
                        }


                }



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
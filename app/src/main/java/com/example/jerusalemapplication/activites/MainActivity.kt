package com.example.jerusalemapplication.activites

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.example.jerusalemapplication.R
import com.example.jerusalemapplication.model.PlayList
import com.example.jerusalemapplication.model.SliderItem
import com.example.jerusalemapplication.adapter.ImageSliderAdapter
import com.example.jerusalemapplication.adapter.PlayListsAdapter
import com.example.jerusalemapplication.datebase.DatabaseHelper
import com.example.jerusalemapplication.model.SharedPref
import com.example.webserviceass.MySingleton
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.ramotion.circlemenu.CircleMenuView
import kotlinx.android.synthetic.main.activity_main.*
import java.io.UnsupportedEncodingException
import java.nio.charset.Charset
import kotlin.math.abs

class MainActivity : AppCompatActivity() {
    val url="https://newsapi.org/v2/everything?q=%D8%A7%D9%84%D9%82%D8%AF%D8%B3&from=2021-06-15&sortBy=popularity&apiKey=0b4a7555206b4f9eada242d9501d5efc"
    var handler = Handler()
    lateinit var db: FirebaseFirestore
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
        setContentView(R.layout.activity_main)
        insertTitle()

        var actionBar = supportActionBar
        actionBar!!.title = resources.getString(R.string.app_name)
        var data = ArrayList<SliderItem>()
        db= Firebase.firestore
        firebaseAnalytics = Firebase.analytics
        view_all.setOnClickListener {
            var i = Intent(this, PlayListsActivity::class.java)
            startActivity(i)
        }

        getThreePlayList()

        trackScreen("Main Screen")
        getToken()
        circleMenu.eventListener = object : CircleMenuView.EventListener() {
            override fun onMenuOpenAnimationStart(view: CircleMenuView) {
                super.onMenuOpenAnimationStart(view)
            }

            override fun onButtonClickAnimationStart(view: CircleMenuView, buttonIndex: Int) {
                super.onButtonClickAnimationStart(view, buttonIndex)
                when(buttonIndex){
                    0 -> {
                        var i = Intent(applicationContext, MainActivity::class.java)
                        startActivity(i)
                    }
                    1 -> {
                        var i = Intent(applicationContext, LibraryActivity::class.java)
                        startActivity(i)
                    }
                    2 -> {
                        var i = Intent(applicationContext, MapsActivity::class.java)
                        startActivity(i)
                    }
                    3 -> {
                        var i = Intent(applicationContext, SettingActivity::class.java)
                        startActivity(i)
                    }
                    4 -> {
                        var i = Intent(applicationContext, ChartActivity::class.java)
                        startActivity(i)
                    }
                }
            }
        }

        val cache= MySingleton.getInstance()!!.getRequestQueue()!!.cache
        val entry=   cache.get(url)
        if (entry != null){
            try {
                val da=String(entry!!.data, Charset.forName("UTF-8"))


            }catch (e: UnsupportedEncodingException){
                Log.e("no", e.message!!)
            }
        }else{

            val jsonObject= object : JsonObjectRequest(
                Request.Method.GET,url,null,
                Response.Listener { response ->

                    val cases=  response.getJSONArray("articles")

                    for (i in 0 until cases.length()){
                        val case=    cases.getJSONObject(i)
                        data.add(SliderItem(case.getString("urlToImage"),case.getString("title"),case.getString("description")
                               , case.getString("url"),case.getString("publishedAt")))

                    }



                    viewPager.adapter = ImageSliderAdapter(this,data)
                    viewPager.clipToPadding = false
                    viewPager.clipChildren = false
                    viewPager.offscreenPageLimit = 3
                    viewPager.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER

                    var compositePageTransformer=  CompositePageTransformer()
                    compositePageTransformer.addTransformer(MarginPageTransformer(20))
                    compositePageTransformer.addTransformer(ViewPager2.PageTransformer { page, position ->
                        var r = 1- abs(position)
                        page.scaleY = 0.85f + r * 0.15f
                    })
                    viewPager.setPageTransformer(compositePageTransformer)
                    viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                        override fun onPageSelected(position: Int) {
                            super.onPageSelected(position)
                            handler.removeCallbacks(sliderRunnable)
                            handler.postDelayed(sliderRunnable,3000)
                        }
                    })

                },
                Response.ErrorListener {error ->
                    Toast.makeText(this@MainActivity,error.message, Toast.LENGTH_SHORT).show()
                }

            ){
                override fun getHeaders(): MutableMap<String, String> {
                    val headers =
                        HashMap<String, String>()

                    headers["User-Agent"] = "Mozilla/5.0"
                    return headers
                }
            }
            MySingleton.getInstance()!!.addRequestQueue(jsonObject)
        }

    }

    private var sliderRunnable = Runnable {
        viewPager.currentItem = viewPager.currentItem + 1
    }

    fun makeToast(msg:String){
        Toast.makeText(this,msg,Toast.LENGTH_LONG).show()
    }

    fun insertTitle(){
        val dbHelper = DatabaseHelper(this)
        dbHelper.insertPlayListName("مع المرابطة هنادي 1","أسماء المسجد الأقصى مع المرابطة هنادي,حقيقة المسجد الأقصى مع المرابطة هنادي,تهويد القدس مع المرابطة هنادي,ضريبة الارنونا مع المرابطة هنادي,التقسيم الزماني مع المرابطة هنادي,المصلى المرواني مع المرابطة هنادي,البلدة القديمة وأقساامها مع المرابطة هنادي,حرمة المسجد الاقصى مع المرابطة هنادي")
        dbHelper.insertPlayListName("يوسف ومريم – الموسم الأول –","يوسف ومريم - تاريخ بيت المقدس,يوسف ومريم - اسم المسجد الأقصى,يوسف ومريم - ما هو المسجد الأقصى؟,يوسف ومريم - حفريات المسجد الأقصى,يوسف ومريم - معالم المسجد الأقصى,يوسف ومريم - علاقة المسجد الأقصى بالمسجد الحرام,يوسف ومريم - فضائل المسجد الاقصى,يوسف ومريم - فضائل المسجد الاقصى2")
        dbHelper.insertPlayListName("فلاشات مقدسية – الموسم الثالث –","التهويد_معلومة مقدسية,هضبة موريا_معلومة مقدسية,قدس واحدة_معلومة مقدسية,مصلى الأنبياء_معلومة مقدسية,ليس حرماً_معلومة مقدسية,ما هو المسجد الأقصى_معلومة مقدسية,التقسيم الزماني والمكاني_معلومة مقدسية,علاقة المسجد الحرام بالمسجد الأقصى")
        dbHelper.insertPlayListName("مع المرابطة هنادي 2","مع المرابطة هنادي الموسم الثاني الحلقة السادسة - الهوية الزرقاء,مع المرابطة هنادي الموسم الثاني الحلقة الخامسة- باب المغاربة,برنامج مع المرابطة هنادي الموسم الثاني الحلقة الثامنة - تسريب العقارات,مع المرابطة هنادي الموسم الثاني الحلقة الثالثة - الرباط والقائمة الذهبية,مع المرابطة هنادي الموسم الثاني الحلقة السابعة - الفتح الصلاحي,مع المرابطة هنادي الموسم الثاني الحلقة الرابعة - التضييق الاقتصادي,مع المرابطة هنادي الموسم الثاني الحلقة الاولى - مصلى باب الرحمة,مع المرابطة هنادي الموسم الثاني الحلقة الثانية - النصارى والقدس")
        dbHelper.insertPlayListName("فلاشات مقدسية – الموسم الثاني –","هدم البيوت / معلومة مقدسية,قانون أملاك الغائبين / معلومة مقدسية,الديانة اليهودية/ معلومة مقدسية,القدس عند اليهود / معلومة مقدسية,الهيكل عند اليهود/ معلومة مقدسية,الحروب الصليبية/ معلومة مقدسية,فقر المقدسيين/ معلومة مقدسية,ترخيص الأبنية/ معلومة مقدسية")
    }

    private fun getThreePlayList(){
        val playList=mutableListOf<PlayList>()

        db.collection("playList").limit(3)
            .get()
            .addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot){

                        playList.add(
                            PlayList(document.getString("img"))
                        )

                }

                rvPlayList.layoutManager = LinearLayoutManager(this,
                    LinearLayoutManager.HORIZONTAL,false)
                rvPlayList.setHasFixedSize(true)
                val restAdapter = PlayListsAdapter(this,"Home",playList)
                rvPlayList.adapter=restAdapter

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

    fun getToken(){
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener {task ->
                if (!task.isSuccessful) {
                    Log.w("TAG", "Fetching FCM registration token failed", task.exception)
                }
               var token = task.result.toString()
                Log.e("token",token)
            }
    }
    }
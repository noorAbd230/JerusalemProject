package com.example.jerusalemapplication.activites

import LibraryAdapter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jerusalemapplication.R
import com.example.jerusalemapplication.adapter.PlayListsAdapter
import com.example.jerusalemapplication.datebase.LibraryDH
import com.example.jerusalemapplication.model.Library
import com.example.jerusalemapplication.model.PlayList
import com.example.jerusalemapplication.model.SharedPref
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_library.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.rvPlayList

class LibraryActivity : AppCompatActivity() {
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
        setContentView(R.layout.activity_library)
        insertItems()
        db= Firebase.firestore
        getLibraryImg()
        var actionBar = supportActionBar
        actionBar!!.title = resources.getString(R.string.app_name)
        firebaseAnalytics = Firebase.analytics
        trackScreen("Library Screen")
    }

    fun insertItems(){
      var db = LibraryDH(this)
        db.insertLibraryItem(
            "قباب المسجد الأقصى\n",
            "يضم المسجد الأقصى المبارك 15 قبة جميلة تضيف له رونق خاص، وتتوزع في محيط المسجد المبارك، حيث بُني أغلبها في الفترة الأموية والأيوبية، كما أن كل قبة لها شكل وطريقة بناء مختلفة. ومن هذه القباب: قبة السلسلة، قبة الصخرة، قبة المعراج، وغيرها من القباب."
        )

        db.insertLibraryItem(
            "بوائك المسجد الأقصى\n",
            "تعتبر البوائك من المعالم العريقة التي تشهد تاريخ مكان معين ، وفي المسجد الأقصى تعد البوائك من الشواهد المهمة على تعاقب العصور في المسجد الأقصى كالمماليك والعباسيين والفاطميين والأمويين.  ويحتوي المسجد الأقصى على 8 بوائك موزعة على صحن قبة الصخرة من جميع الجهات، والبائكة هي مجموعة من الأعمدة على خط مستقيم تصل فوقها بأقواس تحمل سقف البائكة.\n"
        )

        db.insertLibraryItem(
            "أروقة المسجد الأقصى\n",
            "يحتوي المسجد الأقصى المبارك على 3 أروقة تاريخية تُضيف له منظر جمالي مميز، حيث تكون هذه الأروقة ملاصقة لأسوار المسجد من جهاته الثلاث الشرقية والشمالية والغربية. والرواق هو عبارة عن ممر طويل مسقوف بطريقة مميزة جداً، ويتكون من أعمدة حجرية موصولة بأقواس تاريخية تستخدم إما للدراسة أو التلاقي أو التشاور"
        )

        db.insertLibraryItem(
            "مصاطب المسجد الأقصى\n",
            "المصطبة هي مكان مرتفع قليلًا عن الأرض ويستخدم للجلوس عليه من قبل طلاب العلم، وفي المسجد الأقصى تستخدم المصاطب للدراسة وقراءة القران وتدبره. يوجد في المسجد الأقصى 41 مصطبة حيث يتواجد الكثير منها في الجهتين الغربية والشرقية للمسجد الأقصى بينما يقل عددها في الجهتين الشمالية والجنوبية.\n"

        )

        db.insertLibraryItem(
            "أبواب المسجد الأقصى\n",
            "يحتوي المسجد الأقصى المبارك على 17 باباً تاريخياً، منها المفتوحة ومنها المغلقة. تتكون الأبواب المفتوحة من 10 أبواب، منها: باب الأسباط، حطة، الغوانمة، السلسلة وغيرها. ويٌقدر عدد الأبواب المغلقة بـ 7 أبواب، منها: باب السكينة القديم، والجنائز، الرحمة والتوبة، المفرد، الثلاثي وباب حطة القديم.\n"
        )



        db.insertLibraryItem(
            "مدارس المسجد الأقصى\n",
            "يضم المسجد الأقصى المبارك ما يٌقارب 19 مدرسة. توجد 13 مدرسة منها في الجهة الشمالية للمسجد، وباقيها في الجهة الغربية منه، وتعد هذه المدارس مدارس تاريخية بٌني معظمها في الفترة الأيوبية والمملوكية، وبعضها أنشئ حديثاً.  كما وقد تحول جزء من هذه المدارس إلى مساكن لبعض العائلات المقدسية ولِأمور أخرى.\n"
        )

        db.insertLibraryItem(
            "أسبلة المسجد الأقصى\n",
            "السبيل هو عبارة عن عين مياه مزود بعدد من الصنابير، تم بناؤها في فترة حُكم المسلمين لمدينة القدس والمسجد الأقصى وخُصوصاً في الفترة المملوكية والأيوبية، يوجد في المسجد الأقصى ما يُقارب 16 سبيلاً، أشهرها: سبيل الكأس وسبيل قايتباي. كما أن بعضها مكشوف وله قابلية لتخزين مياه الأمطار، وتم استخدام هذه الأسبلة للشرب والوضوء، كما وَتم تجديد العديد منها قديماً على يد الدولة العثمانية وحديثاً بواسطة لجان الإعمار.\n" +
                    "\n"
        )



    }

    private fun getLibraryImg(){
        val library=mutableListOf<Library>()

        db.collection("library")
            .get()
            .addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot){

                    library.add(
                        Library(document.getString("img"))
                    )

                }

                rvLibrary.layoutManager = LinearLayoutManager(this,
                    LinearLayoutManager.VERTICAL,false)
                rvLibrary.setHasFixedSize(true)
                val restAdapter = LibraryAdapter(this,library)
                rvLibrary.adapter=restAdapter

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
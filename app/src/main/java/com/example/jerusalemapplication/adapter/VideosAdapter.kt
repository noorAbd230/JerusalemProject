package com.example.jerusalemapplication.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.jerusalemapplication.R
import com.example.jerusalemapplication.activites.VideoDisplayActivity
import com.example.jerusalemapplication.datebase.DatabaseHelper
import com.example.jerusalemapplication.model.Video
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.videos_item.view.*


class VideosAdapter(var activity: Activity,var title : String, var data: MutableList<Video>) :
    RecyclerView.Adapter<VideosAdapter.MyViewHolder>()  {


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val title = itemView.video_title
        val img = itemView.img_video
        val card= itemView.video_card

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

            val itemView = LayoutInflater.from(activity).inflate(R.layout.videos_item, parent, false)
          return  MyViewHolder(itemView)

    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val dbHelper = DatabaseHelper(activity)
        val lstValues = ArrayList<String>()

                  var str = dbHelper.getVideosName(title)[0].vName

                   var strSep = str.split(",")
                   for (i in 0 until strSep.size){
                       var titles = strSep[i].toString()
                       lstValues.add(titles)
                   }

              holder.title.text = lstValues[position]
            Picasso.get().load(data[position].img).into(holder.img)

        holder.img.animation = AnimationUtils.loadAnimation(activity,R.anim.fade_transition_animation)
        holder.card.animation = AnimationUtils.loadAnimation(activity,R.anim.fade_scale_animation)

            holder.card.setOnClickListener {
                var i = Intent(activity, VideoDisplayActivity::class.java)
                i.putExtra("url",data[position].url)
                activity.startActivity(i)
            }






    }


    fun makeToast(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
    }



}
package com.example.jerusalemapplication.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.jerusalemapplication.activites.PlaylistVideosActivity
import com.example.jerusalemapplication.model.PlayList
import com.example.jerusalemapplication.R
import com.example.jerusalemapplication.datebase.DatabaseHelper
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.video_item.view.*
import kotlinx.android.synthetic.main.video_two_item.view.*


class PlayListsAdapter(var activity: Activity, var name:String , var data: MutableList<PlayList>) :
    RecyclerView.Adapter<PlayListsAdapter.MyViewHolder>()  {


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val title = itemView.playList_title
        val img = itemView.img_playList
        val card= itemView.playList_card
        val gridTitle = itemView.grid_playList_title
        val gridImg = itemView.grid_img_playList
        val gridCard= itemView.grid_playList_card

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return if (name=="Home") {
            val itemView = LayoutInflater.from(activity).inflate(R.layout.video_item, parent, false)
            MyViewHolder(itemView)
        }else {
            val itemView =
                LayoutInflater.from(activity).inflate(R.layout.video_two_item, parent, false)
            MyViewHolder(itemView)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val dbHelper = DatabaseHelper(activity)

        if (name=="Home"){
            holder.title.text = dbHelper.getThreePlayList()[position].title
            Picasso.get().load(data[position].img).into(holder.img)
            holder.card.setOnClickListener {
                var i = Intent(activity, PlaylistVideosActivity::class.java)
                i.putExtra("img",data[position].img)
                i.putExtra("title",dbHelper.getThreePlayList()[position].title)
                i.putExtra("from","home")
                activity.startActivity(i)
            }
        }else{
            holder.gridTitle.text = dbHelper.getAllPlayList()[position].title
            Picasso.get().load(data[position].img).into(holder.gridImg)

            holder.gridImg.animation = AnimationUtils.loadAnimation(activity,R.anim.fade_transition_animation)
            holder.gridCard.animation = AnimationUtils.loadAnimation(activity,R.anim.fade_scale_animation)
            holder.gridCard.setOnClickListener {
                var i = Intent(activity, PlaylistVideosActivity::class.java)
                i.putExtra("gridImg",data[position].img)
                i.putExtra("gridTitle",dbHelper.getAllPlayList()[position].title)
                i.putExtra("from","grid")
                activity.startActivity(i)


            }

            }





    }


    fun makeToast(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
    }



}
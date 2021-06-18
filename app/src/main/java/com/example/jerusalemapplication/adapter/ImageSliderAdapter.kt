package com.example.jerusalemapplication.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.jerusalemapplication.activites.NewsDetailsActivity
import com.example.jerusalemapplication.model.SliderItem
import com.example.jerusalemapplication.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.slider_item.view.*


class ImageSliderAdapter(var activity: Activity, var data: MutableList<SliderItem>) : RecyclerView.Adapter<ImageSliderAdapter.MyViewHolder>() {
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        val imageSlider = itemView.imageSlider
        val title = itemView.title

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(activity).inflate(R.layout.slider_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        Picasso.get().load(data[position].img).into(holder.imageSlider)
        holder.title.text = data[position].title
        holder.imageSlider.setOnClickListener {
            var i = Intent(activity, NewsDetailsActivity::class.java)
            i.putExtra("title",data[position].title)
            i.putExtra("img",data[position].img)
            i.putExtra("desc",data[position].desc)
            i.putExtra("date",data[position].publishedAt)
            i.putExtra("url",data[position].url)
            activity.startActivity(i)
        }
    }

//    private var runnable = Runnable {
//        data.addAll(data)
//        notifyDataSetChanged()
//    }
}
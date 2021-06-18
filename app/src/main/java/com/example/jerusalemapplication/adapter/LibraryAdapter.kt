
import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.example.jerusalemapplication.R
import com.example.jerusalemapplication.activites.LibraryDetailsActivity
import com.example.jerusalemapplication.datebase.LibraryDH
import com.example.jerusalemapplication.model.Library
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.library_item.view.*


class LibraryAdapter(var activity: Activity, var data: MutableList<Library>) :
    RecyclerView.Adapter<LibraryAdapter.MyViewHolder>() {


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title = itemView.tvTitlecard
        val Details = itemView.tvDetailscard
        val image = itemView.imgcard
        val card = itemView.card

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(activity).inflate(R.layout.library_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
          var db = LibraryDH(activity)

        holder.title.text =  db.getLibraryData()[position].Title
        holder.Details.text = db.getLibraryData()[position].detail

        Picasso.get().load(data[position].img).into(holder.image)
        holder.image.animation = AnimationUtils.loadAnimation(activity,R.anim.fade_transition_animation)
        holder.card.animation = AnimationUtils.loadAnimation(activity,R.anim.fade_scale_animation)


        holder.card.setOnClickListener {

            val i = Intent(activity!!, LibraryDetailsActivity::class.java)
            i.putExtra("Title", db.getLibraryData()[position].Title)
            i.putExtra("Details", db.getLibraryData()[position].detail)
            i.putExtra("image", data[position].img)
            activity.startActivity(i)

        }




    }


}
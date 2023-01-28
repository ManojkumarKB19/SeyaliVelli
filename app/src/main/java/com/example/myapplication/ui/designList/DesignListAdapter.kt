package com.example.myapplication.ui.designList

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.GenericTransitionOptions.with
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions.with
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.with
import com.example.myapplication.R
import com.example.myapplication.di.DesignListDataModel
import com.example.myapplication.ui.home.DesignCategoryAdapter
import com.example.myapplication.util.RotateTransformation
import com.squareup.picasso.Picasso

class DesignListAdapter(private val mCtx: Context,private var designList:ArrayList<DesignListDataModel>,val mClickListener: DesignListAdapter.DesignItemClickListener) : RecyclerView.Adapter<DesignListViewHolder>() {

    fun setItems(list:ArrayList<DesignListDataModel>){
        designList = list
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DesignListViewHolder {
        val itemView = LayoutInflater.from(mCtx).inflate(R.layout.item_ornament,parent, false)
        return DesignListViewHolder(itemView,mClickListener)
    }

    override fun onBindViewHolder(holder: DesignListViewHolder, position: Int) {
        holder.designName.text = designList.get(position).Key +  " "
        //+ designList.get(position).imageUrl + " " + designList.get(position).inches + " " + designList.get(position).weight
        if (!designList.get(position).ImageUrl.isEmpty()) {
            /*Glide.with(mCtx)
                .load(designList.get(position).ImageUrl)
                .transform(RotateTransformation(mCtx, 270f))
                .placeholder(android.R.drawable.presence_online)
                .into(holder.designImage)*/

            Picasso.get()
                .load(designList.get(position).ImageUrl)
                .resize(1500, 0)
                .rotate(270f)
                .placeholder(android.R.drawable.progress_indeterminate_horizontal)
                .error(R.drawable.ic_launcher_background)
                .into(holder.designImage);
        } else {
            holder.designImage.setImageResource(R.drawable.ic_launcher_background)
        }

        Log.d("DeisgnAdapter",designList.get(position).Size?.size.toString())
    }

    override fun getItemCount(): Int {
        return designList.size
    }

    interface DesignItemClickListener {
        fun onDesignItemClick(view: View?, position: Int)
    }

}
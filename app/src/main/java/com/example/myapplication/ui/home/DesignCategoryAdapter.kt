package com.example.myapplication.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.dm.DesignCategoryModel

class DesignCategoryAdapter(private val designCategoryList: ArrayList<DesignCategoryModel>, private val context: Context, val mClickListener: DesignCategoryAdapter.ItemClickListener?) : RecyclerView.Adapter<DesignCategoryAdapter.ViewHolder>() {

    class ViewHolder internal constructor(itemView: View, mClickListener: DesignCategoryAdapter.ItemClickListener) : RecyclerView.ViewHolder(itemView),View.OnClickListener {
        var txtDesignCategory: TextView
        var mClick = mClickListener

        override fun onClick(view: View?) {
            mClick.onItemClick(view, adapterPosition)
        }

        init {
            txtDesignCategory = itemView.findViewById(R.id.idDesignCategory)
            itemView.setOnClickListener(this)
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DesignCategoryAdapter.ViewHolder {
        // this method is use to inflate the layout file
        // which we have created for our recycler view.
        // on below line we are inflating our layout file.
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.item_design_categories,
            parent, false
        )
        // at last we are returning our view holder
        // class with our item View File.
        return DesignCategoryAdapter.ViewHolder(itemView, mClickListener!!)
    }

    override fun onBindViewHolder(holder: DesignCategoryAdapter.ViewHolder, position: Int) {
        // on below line we are setting data to our text view and our image view.
        holder.txtDesignCategory.text = designCategoryList.get(position).designCategoryModel?.let { it }?:""
    }

    override fun getItemCount(): Int {
        return designCategoryList.size
    }

    // parent activity will implement this method to respond to click events
    interface ItemClickListener {
        fun onItemClick(view: View?, position: Int)
    }

}
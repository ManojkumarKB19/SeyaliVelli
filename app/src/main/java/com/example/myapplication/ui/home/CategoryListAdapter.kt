package com.example.myapplication.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.di.CategoryModel


class CategoryListAdapter(private val categoryList: ArrayList<CategoryModel>,
                          private val context: Context,
                          val mClickListener: ItemClickListener?) : RecyclerView.Adapter<CategoryListAdapter.ViewHolder>() {

    // data is passed into the constructor

    class ViewHolder internal constructor(itemView: View,mClickListener: ItemClickListener) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        var myTextView: TextView
        var mClick = mClickListener

       override fun onClick(view: View?) {
           mClick.onItemClick(view, adapterPosition)
        }

        init {
            myTextView = itemView.findViewById(R.id.txtCategoryName)
            itemView.setOnClickListener(this)
        }

    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CategoryListAdapter.ViewHolder {
        // this method is use to inflate the layout file
        // which we have created for our recycler view.
        // on below line we are inflating our layout file.
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.item_category,
            parent, false
        )
        // at last we are returning our view holder
        // class with our item View File.
        return ViewHolder(itemView,mClickListener!!)
    }

    override fun onBindViewHolder(holder: CategoryListAdapter.ViewHolder, position: Int) {
        // on below line we are setting data to our text view and our image view.
        holder.myTextView.text = categoryList.get(position).categoryName?.let { it }?:""
    }


    override fun getItemCount(): Int {
        return categoryList.size
    }

    // parent activity will implement this method to respond to click events
    interface ItemClickListener {
        fun onItemClick(view: View?, position: Int)
    }
}
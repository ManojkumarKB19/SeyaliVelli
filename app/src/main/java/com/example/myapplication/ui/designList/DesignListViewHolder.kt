package com.example.myapplication.ui.designList

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.ui.home.DesignCategoryAdapter

class DesignListViewHolder(private var itemView: View, mClickListener: DesignListAdapter.DesignItemClickListener): RecyclerView.ViewHolder(itemView),View.OnClickListener {

    var designName:TextView
    var designImage:ImageView
    var mClick = mClickListener

    init {
        designName = itemView.findViewById(R.id.txtName)
        designImage = itemView.findViewById(R.id.imgDesign)
        itemView.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        mClick.onDesignItemClick(view, adapterPosition)
    }

}
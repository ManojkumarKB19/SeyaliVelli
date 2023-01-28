package com.example.myapplication.ui.designDetail

import android.view.View
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.ui.designList.DesignListAdapter

class DesignSizeListViewHolder (private var itmView: View, mClickListener: DesignSizeListAdapter.DesignSizeItemClickListener): RecyclerView.ViewHolder(itmView),
    View.OnClickListener {

    var designLength: TextView
    var designWeight: TextView
    var checkBox: CheckBox
    var quantityLabel: TextView
    var quantityValueLayout:ConstraintLayout
    var icAdd:ImageView
    var txtQuantityValue:TextView
    var icSub:ImageView
   lateinit var itemVw:View
    var mClick = mClickListener

    init {
        itemVw = itmView
        designLength = itemView.findViewById(R.id.txtLength)
        designWeight = itemView.findViewById(R.id.txtWeight)
        checkBox = itemView.findViewById(R.id.idCheckBox)
        quantityValueLayout = itemVw.findViewById(R.id.quantityValueLayout)
        quantityLabel = itemVw.findViewById(R.id.txtQuantity)
        icAdd = itemVw.findViewById(R.id.ic_add)
        icSub = itemVw.findViewById(R.id.ic_minus)
        txtQuantityValue = itemVw.findViewById(R.id.quantityValue)
        itemView.setOnClickListener(this)
        icAdd.setOnClickListener(this)
        icSub.setOnClickListener(this)
        checkBox.setOnClickListener(this)

    }

    override fun onClick(view: View?) {
        if((view?.id ==R.id.idCheckBox)){
            mClick.onDesignSizeItemClick(view, adapterPosition)
        }else if(view?.id == R.id.ic_add){
            mClick.onAddClick(view,adapterPosition)
        }else if(view?.id == R.id.ic_minus){
            mClick.onSubClick(view,adapterPosition)
        }else{
            mClick.onDesignSizeItemClick(view, adapterPosition)
        }
    }

}
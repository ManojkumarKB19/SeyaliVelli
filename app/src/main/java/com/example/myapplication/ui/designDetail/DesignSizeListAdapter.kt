package com.example.myapplication.ui.designDetail

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.di.DesignListDataModel
import com.example.myapplication.di.SizeItem
import com.google.gson.Gson
import com.google.gson.JsonObject


class DesignSizeListAdapter (private val mCtx: Context, private var designSizeList:ArrayList<SizeItem>, val mClickListener: DesignSizeListAdapter.DesignSizeItemClickListener) : RecyclerView.Adapter<DesignSizeListViewHolder>() {

    fun setItems(list:ArrayList<SizeItem>){
        designSizeList = list
        notifyDataSetChanged()
    }

    fun setThisItem(position: Int,sizeItem: SizeItem){
        designSizeList.set(position,sizeItem)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DesignSizeListViewHolder {
        val itemView = LayoutInflater.from(mCtx).inflate(R.layout.item_design_size,parent, false)
        return DesignSizeListViewHolder(itemView,mClickListener)
    }

    override fun onBindViewHolder(holder: DesignSizeListViewHolder, position: Int) {

        var gson = Gson()
        val jsonObject: JsonObject = gson.toJsonTree(designSizeList.get(position)).getAsJsonObject()

       var designSizeItem  = gson.fromJson(jsonObject, SizeItem::class.java)
        Log.d("DesignAdapter",designSizeList.get(position).toString())
     //  var sizeItem = designSizeList.get(position)  as SizeItem
        holder.designLength.text = designSizeItem.length
        holder.designWeight.text = designSizeItem.weight
        holder.txtQuantityValue.text = designSizeItem.quantities.toString()

        if(designSizeItem.isSelected){
            holder.itemVw.background = mCtx.resources.getDrawable(R.drawable.rectangle_stroke_red,null)
            holder.checkBox.isChecked = true
            quantityVisible(true,holder)
        }else{
            holder.itemVw.background = mCtx.resources.getDrawable(R.drawable.rectangle,null)
            holder.checkBox.isChecked = false
            quantityVisible(false,holder)
        }


        //Log.d("DeisgnAdapter","Length " +designSizeList.getValue("Length").toString()+ " weight "+designSizeList.getValue("Weight"))
    }

    private fun quantityVisible(b: Boolean,holder: DesignSizeListViewHolder) {
        if(b){
            holder.quantityValueLayout.visibility = View.VISIBLE
            holder.quantityLabel.visibility = View.VISIBLE
        }else{
            holder.quantityValueLayout.visibility = View.GONE
            holder.quantityLabel.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int {
        return designSizeList.size
    }

    interface DesignSizeItemClickListener {
        fun onDesignSizeItemClick(view: View?, position: Int)
        fun onAddClick(view: View?,position: Int)
        fun onSubClick(view: View?,position: Int)
    }

}